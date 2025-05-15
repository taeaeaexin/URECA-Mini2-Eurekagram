window.onload = () => {
    const token = sessionStorage.getItem("jwt");
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
                if (!feed || !feed.feedId) return;

                const feedDiv = document.createElement('div');
                feedDiv.className = "feed-card";

                const image = feed.images?.[0]
                    ? `<img src="${feed.images[0]}" class="feed-image" />`
                    : `<div class="feed-image-placeholder">이미지 없음</div>`;

                // 날짜 포맷 예시 (정확하게 yyyy-MM-dd HH:mm:ss로 추출)
                const formatDateTime = (isoString) => {
                    if (!isoString) return '';
                    const date = new Date(isoString);
                    const yyyy = date.getFullYear();
                    const mm = String(date.getMonth() + 1).padStart(2, '0');
                    const dd = String(date.getDate()).padStart(2, '0');
                    const hh = String(date.getHours()).padStart(2, '0');
                    const mi = String(date.getMinutes()).padStart(2, '0');
                    return `${yyyy}년 ${mm}월 ${dd}일 ${hh}시 ${mi}분 `;
                };

                // 사용 예시
                const date = formatDateTime(feed.createDate);

                feedDiv.innerHTML = `
                                    <div class="feed-header">
                                        <div class="nickname">@${feed.nickName ?? '알 수 없음'}</div>
                                        <div class="created-date">${date}</div>
                                    </div>
                                    ${image}
                                    <div class="feed-content">${feed.content}</div>
                                    <div class="feed-meta">조회수 ${feed.viewCount} · 댓글 ${feed.commentCount}</div>
                                `;

                feedDiv.addEventListener('click', () => {
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
                        mainContent.innerHTML = `
                                                <div class="empty-message">게시물이 없습니다</div>
                                            `;
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

            // ✅ 세션 복원 제거하고 바로 loadFeeds()
            loadFeeds();

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

                    // 🔥 검색창을 제외한 나머지 mainContent 내부 요소 제거
                    const mainContent = document.querySelector('.main-content');
                    Array.from(mainContent.children).forEach(child => {
                        if (!child.classList.contains('top-navbar')) {
                            mainContent.removeChild(child);
                        }
                    });

                    loadFeeds();
                }
            });

            // 로그아웃
            window.logout = () => {
                sessionStorage.removeItem("jwt");
                window.location.href = "/";
            };

        } catch (err) {
            console.error("인증 실패:", err);
            window.location.href = "/";
        }
    }
};