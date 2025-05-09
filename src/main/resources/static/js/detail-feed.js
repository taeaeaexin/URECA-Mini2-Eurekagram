let userId; // 현재 로그인한 사용자 ID
let token;

// 즉시 실행되는 인증 확인 로직 (JWT 검사 및 인증)
(async () => {
    token = localStorage.getItem("jwt");

    if (!token) {
        console.log("jwt 토큰이 존재하지 않습니다.");
        window.location.href = "/";
        return;
    }

    try {
        const response = await fetch("/page/authenticate", {
            method: "POST",
            headers: {
                "Authorization": `Bearer ${token}`
            }
        });

        if (response.ok) {
            document.body.classList.remove("hidden");
        } else {
            console.warn("JWT 인증 실패");
            window.location.href = "/";
        }
    } catch (e) {
        console.error("요청 실패:", e);
        window.location.href = "/";
    }
})();

// 상세 페이지 렌더링 로직 (window.onload 이후 실행)
window.onload = () => {
    const params = new URLSearchParams(location.search);
    const feedId = params.get("id");

    if (!feedId) {
        alert("잘못된 접근입니다. 피드 ID가 없습니다.");
        return;
    }

    // 피드 데이터 조회
    fetch(`/feeds/${feedId}`, {
        method: "GET",
        headers: {
            "Authorization": `Bearer ${token}`
        }
    })
        .then(res => {
            if (!res.ok) throw new Error("피드 조회 실패");
            return res.json();
        })
        .then(feed => {
            // 이미지 렌더링
            const img = document.getElementById("feed-image");
            const placeholder = document.getElementById("image-placeholder");

            if (feed.imageUrl) {
                img.src = feed.imageUrl;
                img.style.display = "block";
                placeholder.style.display = "none";
            }

            // 글 내용 렌더링
            document.getElementById("feed-content").textContent = feed.content;

            // 댓글 목록 렌더링
            renderComments(feed.comments);
        })
        .catch(err => {
            console.error("피드 불러오기 오류", err);
            alert("피드 정보를 불러오지 못했습니다.");
        });

    // 댓글 등록 이벤트
    document.getElementById("comment-submit").addEventListener("click", async () => {
        const input = document.getElementById("comment-input");
        const content = input.value.trim();

        if (!content) return;

        try {
            const res = await fetch("/api/comments", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${token}`
                },
                body: JSON.stringify({
                    feedId: feedId,
                    content: content
                })
            });

            if (!res.ok) throw new Error();

            input.value = "";
            location.reload(); // 간단히 새로고침
        } catch {
            alert("댓글 등록 실패");
        }
    });
};

// 댓글 목록 렌더링 함수
function renderComments(comments) {
    const list = document.getElementById("comment-list");
    list.innerHTML = "";

    comments.forEach(comment => {
        const row = document.createElement("div");
        row.classList.add("d-flex", "justify-content-between", "align-items-center", "mb-1");

        const content = document.createElement("span");
        content.textContent = comment.content;

        const delBtn = document.createElement("button");
        delBtn.textContent = "삭제";
        delBtn.className = "btn btn-outline-light btn-sm";
        delBtn.disabled = !comment.isMine;

        delBtn.onclick = () => deleteComment(comment.id);

        row.appendChild(content);
        row.appendChild(delBtn);
        list.appendChild(row);
    });
}

// 댓글 삭제 함수
async function deleteComment(commentId) {
    try {
        const res = await fetch(`/api/comments/${commentId}`, {
            method: "DELETE",
            headers: {
                "Authorization": `Bearer ${token}`
            }
        });

        if (!res.ok) throw new Error();
        location.reload();
    } catch {
        alert("댓글 삭제 실패");
    }
}