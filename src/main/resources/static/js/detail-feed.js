let token;
let userId;
let feedId;

// ✅ JWT 인증 및 인증 완료 후 본문 렌더링 시작
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
            window.location.href = "/";
        }
    } catch (e) {
        console.error("요청 실패:", e);
        window.location.href = "/";
    }
})();

window.onload = async () => {
    try {
        const params = new URLSearchParams(location.search);
        feedId = params.get("id");

        if (!feedId) {
            alert("잘못된 접근입니다. 피드 ID가 없습니다.");
            return;
        }

        const res = await fetch(`/feeds/${feedId}`, {
            method: "GET",
            headers: {
                "Authorization": `Bearer ${token}`
            }
        });

        if (!res.ok) throw new Error("피드 조회 실패");

        const result = await res.json();
        const feed = result.data;
        userId = feed.writer.userId;

        console.log("[DEBUG] feed:", feed);

        renderImages(feed.imageDtoList);
        document.getElementById("feed-content").textContent = feed.content;

        // ✅ 수정/삭제 버튼 표시 조건
        if (feed.deleteUpdateYn === true) {
            const editBtn = document.getElementById("edit-feed-btn");
            const deleteBtn = document.getElementById("delete-feed-btn");
            editBtn.classList.remove("d-none");
            deleteBtn.classList.remove("d-none");

            editBtn.onclick = () => {
                alert("수정 기능은 추후 구현 예정입니다.");
                location.href = "/page/modify-feed";
            };

            deleteBtn.onclick = async () => {
                if (!confirm("피드를 삭제하시겠습니까?")) return;

                try {
                    const res = await fetch(`/feeds/${feedId}`, {
                        method: "DELETE",
                        headers: {
                            "Authorization": `Bearer ${token}`
                        }
                    });

                    if (!res.ok) throw new Error("피드 삭제 실패");

                    alert("피드가 삭제되었습니다.");
                    location.href = "/page/main";
                } catch (err) {
                    console.error("피드 삭제 오류", err);
                    alert("피드 삭제에 실패했습니다.");
                }
            };
        }

        await loadComments(); // 댓글 렌더링
        bindCommentSubmit();  // 댓글 등록 이벤트

    } catch (err) {
        console.error("피드 불러오기 오류", err);
        alert("피드 정보를 불러오지 못했습니다.");
        location.href = "/page/main";
    }
};

// ✅ 댓글 조회
async function loadComments() {
    try {
        const res = await fetch("/comments/all", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${token}`
            },
            body: JSON.stringify({
                feedId: Number(feedId),
                userId: Number(userId)
            })
        });

        if (!res.ok) throw new Error("댓글 목록 조회 실패");

        const result = await res.json();
        renderComments(result.data);
    } catch (err) {
        console.error("댓글 조회 오류", err);
        alert("댓글 목록을 불러오는 데 실패했습니다.");
    }
}

// ✅ 댓글 등록
function bindCommentSubmit() {
    document.getElementById("comment-submit").addEventListener("click", async () => {
        const input = document.getElementById("comment-input");
        const content = input.value.trim();

        if (!content) {
            alert("댓글을 입력해주세요.");
            return;
        }

        try {
            const res = await fetch("/comments/", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${token}`
                },
                body: JSON.stringify({
                    feedId: Number(feedId),
                    userId: Number(userId),
                    content: content
                })
            });

            if (!res.ok) throw new Error("댓글 등록 실패");

            input.value = "";
            await loadComments();
        } catch (err) {
            console.error("댓글 등록 오류", err);
            alert("댓글 등록에 실패했습니다.");
        }
    });
}

// ✅ 이미지 렌더링
function renderImages(imageDtoList = []) {
    const imgContainer = document.getElementById("feed-image-container");
    imgContainer.innerHTML = "";

    if (imageDtoList.length === 0) {
        imgContainer.innerHTML = "<div class='text-muted'>이미지가 없습니다.</div>";
        return;
    }

    imageDtoList.forEach(image => {
        const wrapper = document.createElement("div");
        wrapper.className = "image-slot";

        const img = document.createElement("img");
        img.src = `/images/${image.storedImageName}`;
        img.alt = image.originalImageName;

        wrapper.appendChild(img);
        imgContainer.appendChild(wrapper);
    });
}

// ✅ 댓글 렌더링
// ✅ 댓글 렌더링
function renderComments(comments = []) {
    const list = document.getElementById("comment-list");
    list.innerHTML = "";

    comments.forEach(c => {
        const row = document.createElement("div");
        row.className = "d-flex justify-content-between align-items-center mb-1";

        // 닉네임 + 댓글 내용
        const content = document.createElement("span");
        content.innerHTML = `<strong class="me-2">${c.writer.nickName} :</strong> ${c.content}`;
        row.appendChild(content);

        // ✅ 삭제 가능할 때만 삭제 버튼 추가
        if (c.deleteYn === true) {
            const delBtn = document.createElement("button");
            delBtn.textContent = "삭제";
            delBtn.className = "btn btn-danger btn-sm";
            delBtn.onclick = () => deleteComment(c.commentId);
            row.appendChild(delBtn);
        }

        list.appendChild(row);
    });
}

// ✅ 댓글 삭제 요청
async function deleteComment(commentId) {
    if (!confirm("댓글을 삭제하시겠습니까?")) return;

    try {
        const res = await fetch(`/comments/${commentId}`, {
            method: "DELETE",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${token}`
            }
        });

        if (!res.ok) throw new Error("댓글 삭제 실패");

        await loadComments();
    } catch (err) {
        console.error("댓글 삭제 오류", err);
        alert("댓글 삭제에 실패했습니다.");
    }
}