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
            window.location.href = "/";
        }
    } catch (e) {
        console.error("요청 실패:", e);
        window.location.href = "/";
    }
})();

// 모든 나머지 로직은 window.onload 이후 실행
window.onload = () => {
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

    document.querySelector('.btn.btn-secondary').addEventListener('click', async () => {
        const formData = new FormData();
        const content = document.querySelector('.textarea-box textarea').value;
        formData.append("content", content);

        for (let i = 1; i <= 3; i++) {
            const input = document.getElementById(`image${i}`);
            if (input.files.length > 0) {
                formData.append("images", input.files[0]);
            }
        }

        try {
            const response = await fetch("/feeds/", {
                method: "POST",
                headers: {
                    Authorization: `Bearer ${token}`
                },
                body: formData
            });

            if (!response.ok) {
                throw new Error("서버 오류");
            }

            const result = await response.json();
            window.location.href = `/page/detail-feed?id=${result.data.feedId}`;

        } catch (error) {
            console.error("업로드 실패", error);
            alert("업로드 중 오류가 발생했습니다.");
        }
    });
};