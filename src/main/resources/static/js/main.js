window.onload = () => {
    const token = localStorage.getItem("jwt");
    if (!token) {
        window.location.href = "/";
        return;
    }

    authenticateAndLoad();

    async function authenticateAndLoad() {
        try {
            const res = await fetch("/page/authenticate", {
                method: "POST",
                headers: {
                    "Authorization": `Bearer ${token}`
                }
            });

            if (!res.ok) {
                window.location.href = "/";
                return;
            }

            document.body.classList.remove("hidden");

            let lastFeedId = null;
            let currentNickname = '';
            let isLoading = false;
            let currentFeedList = [];

            const mainContent = document.querySelector('.main-content');

            // 피드 렌더 함수
            function renderFeed(feed) {
                if (!feed || !feed.feedId) return; // 방어 코드

                const feedDiv = document.createElement('div');
                feedDiv.className = "feed-card";

                const image = feed.images?.[0]
                    ? `<img src="${feed.images[0]}" class="feed-image" />`
                    : `<div class="feed-image-placeholder">이미지 없음</div>`;

                feedDiv.innerHTML = `
                    ${image}
                    <div class="feed-content">${feed.content}</div>
                    <div class="feed-meta">조회수 ${feed.viewCount} · 댓글 ${feed.commentCount}</div>
                `;

                feedDiv.addEventListener('click', () => {
                    // ✅ 피드 목록 + 스크롤 위치 저장
                    sessionStorage.setItem("feedList", JSON.stringify(currentFeedList));
                    sessionStorage.setItem("scrollY", window.scrollY.toString());
                    window.location.href = `/page/detail-feed?id=${feed.feedId}`;
                });

                mainContent.appendChild(feedDiv);
            }

            // 피드 로딩 함수
            async function loadFeeds() {
                if (isLoading) return;
                isLoading = true;

                let url = `/feeds?size=5`;
                if (lastFeedId) url += `&lastFeedId=${lastFeedId}`;
                if (currentNickname) url += `&nickname=${currentNickname}`;

                try {
                    const response = await fetch(url, {
                        headers: {
                            "Authorization": `Bearer ${token}`
                        }
                    });

                    if (!response.ok) {
                        console.error("피드 응답 실패", response.status);
                        return;
                    }

                    const feeds = await response.json();

                    if (feeds.length === 0 && !lastFeedId) {
                        mainContent.innerHTML = "<p>게시물이 없습니다</p>";
                        return;
                    }

                    feeds.forEach(feed => {
                        renderFeed(feed);
                        currentFeedList.push(feed);
                        lastFeedId = feed.feedId;
                    });
                } catch (err) {
                    console.error("피드 로드 실패:", err);
                } finally {
                    isLoading = false;
                }
            }

            // 세션 저장 복원 함수
            function restoreFromSession() {
                const feedListJson = sessionStorage.getItem("feedList");
                if (!feedListJson) return false;

                try {
                    const feeds = JSON.parse(feedListJson);
                    feeds.forEach(feed => {
                        renderFeed(feed);
                        currentFeedList.push(feed);
                        lastFeedId = feed.feedId;
                    });

                    const scrollY = sessionStorage.getItem("scrollY");
                    if (scrollY !== null) {
                        setTimeout(() => {
                            window.scrollTo(0, parseInt(scrollY));
                            sessionStorage.removeItem("scrollY");
                            sessionStorage.removeItem("feedList");
                        }, 100); // DOM 렌더링 기다림
                    }
                    return true;
                } catch (err) {
                    console.error("세션 복원 실패", err);
                    return false;
                }
            }

            // 초기 로딩
            const restored = restoreFromSession();
            if (!restored) loadFeeds();

            // 무한 스크롤
            window.addEventListener('scroll', () => {
                if (window.innerHeight + window.scrollY >= document.body.offsetHeight - 100) {
                    loadFeeds();
                }
            });

            // 검색
            document.querySelector('.nav-search').addEventListener('keypress', e => {
                if (e.key === 'Enter') {
                    currentNickname = e.target.value.trim();
                    lastFeedId = null;
                    currentFeedList = [];
                    mainContent.innerHTML = '';
                    loadFeeds();
                }
            });

            // 로그아웃
            window.logout = () => {
                localStorage.removeItem("jwt");
                window.location.href = "/";
            };

        } catch (err) {
            console.error("인증 실패:", err);
            window.location.href = "/";
        }
    }
};