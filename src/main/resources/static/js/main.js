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
            let isLoading = false; // ✅ 전역 위치로 이동

            const mainContent = document.querySelector('.main-content');

            async function loadFeeds() {
                if (isLoading) return;
                isLoading = true; // 🔒 요청 중
                console.log("[로딩 시작]");

                let url = `/feeds?size=5`;
                if (lastFeedId) url += `&lastFeedId=${lastFeedId}`;
                if (currentNickname) url += `&nickname=${currentNickname}`;

                console.log("📡 요청 URL:", url);

                try {
                    const response = await fetch(url, {
                        headers: {
                            "Authorization": `Bearer ${token}`
                        }
                    });

                    console.log("📦 응답 상태코드:", response.status);

                    const feeds = await response.json();
                    console.log("📥 받은 피드 데이터:", feeds);

                    if (feeds.length === 0 && !lastFeedId) {
                        mainContent.innerHTML = "<p>게시물이 없습니다</p>";
                        return;
                    }

                    feeds.forEach(feed => {
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
                            window.location.href = `/page/detail-feed?id=${feed.feedId}`;
                        });

                        mainContent.appendChild(feedDiv);
                        lastFeedId = feed.feedId;
                    });
                } catch (err) {
                    console.error("❌ 피드 로드 실패:", err);
                } finally {
                    isLoading = false; // 🔓 요청 완료
                    console.log("[로딩 종료]");
                }
            }

            // 최초 1회 로딩
            loadFeeds();

            // 무한 스크롤
            window.addEventListener('scroll', () => {
                if (window.innerHeight + window.scrollY >= document.body.offsetHeight - 100) {
                    loadFeeds();
                }
            });

            // 검색 기능
            document.querySelector('.nav-search').addEventListener('keypress', e => {
                if (e.key === 'Enter') {
                    currentNickname = e.target.value.trim();
                    lastFeedId = null;
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
            console.error("❌ 인증 실패:", err);
            window.location.href = "/";
        }
    }
};