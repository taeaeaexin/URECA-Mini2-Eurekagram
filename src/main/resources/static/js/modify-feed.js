let token;
let feedId;

window.onload = async () => {
    // 1. 인증 확인
    token = localStorage.getItem("jwt");
    if (!token) {
        console.log("JWT 토큰이 존재하지 않습니다.");
        window.location.href = "/";
        return;
    }

    try {
        const res = await fetch("/page/authenticate", {
            method: "POST",
            headers: {
                Authorization: `Bearer ${token}`
            }
        });

        if (!res.ok) {
            window.location.href = "/";
            return;
        }

        document.body.classList.remove("hidden");
        init(); // 인증 후 초기화

    } catch (err) {
        console.error("인증 실패:", err);
        window.location.href = "/";
    }

    // 2. 이미지 미리보기 및 삭제 기능 등록
    for (let i = 1; i <= 3; i++) {
        const input = document.getElementById(`image${i}`);
        const preview = document.getElementById(`preview${i}`);
        const deleteBtn = document.getElementById(`delete${i}`);
        const placeholder = input.previousElementSibling.querySelector(".placeholder");

        input.addEventListener("change", () => {
            const file = input.files[0];
            if (file) {
                const reader = new FileReader();
                reader.onload = e => {
                    preview.src = e.target.result;
                    preview.classList.remove("d-none");
                    placeholder.style.display = "none";
                    deleteBtn.style.display = "block";
                };
                reader.readAsDataURL(file);
            }
        });

        deleteBtn.addEventListener("click", (e) => {
            e.preventDefault();
            input.value = "";
            preview.src = "";
            preview.classList.add("d-none");
            placeholder.style.display = "block";
            deleteBtn.style.display = "none";
        });
    }

    // 3. 수정 버튼 클릭 시 처리
    document.getElementById('updateBtn').addEventListener('click', handleUpdate);
};

// 4. URL에서 feedId 추출 + 피드 데이터 조회
function init() {
    const params = new URLSearchParams(window.location.search);
    feedId = params.get("id");

    if (!feedId) {
        alert("잘못된 접근입니다.");
        history.back();
        return;
    }

    fetchFeedDetail(feedId);
}

// 5. 피드 상세 정보 가져오기 → 본문 & 이미지 표시
async function fetchFeedDetail(id) {
    try {
        const res = await fetch(`/feeds/${id}`, {
            headers: {
                Authorization: `Bearer ${token}`
            }
        });

        if (!res.ok) {
            throw new Error("피드를 불러올 수 없습니다.");
        }

        const result = await res.json();
        const feed = result.data;

        // 본문 표시
        document.querySelector('#feedContent').value = feed.content;

        // 이미지 표시 (최대 3개)
        const imageList = feed.imageDtoList || [];
        for (let i = 0; i < imageList.length && i < 3; i++) {
            const imageDto = imageList[i];
            const preview = document.getElementById(`preview${i + 1}`);
            const deleteBtn = document.getElementById(`delete${i + 1}`);
            const placeholder = document.querySelector(`#image${i + 1}`).previousElementSibling.querySelector(".placeholder");

            // ✅ 이미지 경로 조합
            preview.src = `/images/${imageDto.storedImageName}`;

            preview.setAttribute("data-id", imageDto.imageId);

            preview.classList.remove("d-none");
            placeholder.style.display = "none";
            deleteBtn.style.display = "block";
        }

    } catch (err) {
        console.error("피드 상세 로드 실패:", err);
        alert("피드를 불러오는 중 문제가 발생했습니다.");
    }
}

// 6. 수정 요청 (PATCH)
async function handleUpdate() {
    const content = document.querySelector('#feedContent').value.trim();
    if (!content) {
        alert("본문 내용을 입력해주세요.");
        return;
    }

    const formData = new FormData();
    formData.append("id", feedId);       // ✅ Feed ID
    formData.append("content", content); // ✅ 본문 내용

    // ✅ 화면에 남아있는 기존 이미지 ID 수집
    for (let i = 1; i <= 3; i++) {
        const preview = document.getElementById(`preview${i}`);
        const input = document.getElementById(`image${i}`);

        const isVisible = !preview.classList.contains("d-none");
        const isOldImage = preview.src.includes("/images/");
        const isNewUpload = input.files.length > 0;

        if (isVisible && isOldImage && !isNewUpload) {
            const imageId = preview.dataset.id; // <img data-id="102" ...>
            if (imageId) {
                formData.append("remainImageIds", imageId); // ✅ ID 리스트로 보냄
            }
        }
    }

    // ✅ 새로 업로드된 이미지 추가
    for (let i = 1; i <= 3; i++) {
        const input = document.getElementById(`image${i}`);
        if (input.files.length > 0) {
            formData.append("images", input.files[0]);
        }
    }

    try {
        const res = await fetch(`/feeds/${feedId}`, {
            method: "PATCH",
            headers: {
                Authorization: `Bearer ${token}` // ❗ Content-Type 지정 X
            },
            body: formData
        });

        if (!res.ok) {
            throw new Error("수정 실패");
        }

        const result = await res.json();
        window.location.href = `/page/detail-feed?id=${feedId}`;
    } catch (err) {
        console.error("수정 실패:", err);
        alert("수정 중 문제가 발생했습니다.");
    }
}