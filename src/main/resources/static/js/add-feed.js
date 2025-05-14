let token;

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

window.onload = () => {
    const fileInput = document.getElementById("image-upload");
    const carouselInner = document.getElementById("carousel-inner");
    const carousel = document.getElementById("carouselExample");
    const carouselInstance = bootstrap.Carousel.getOrCreateInstance(carousel);
    const imageFiles = [];

    updateCarouselPlaceholder();

    fileInput.addEventListener("change", () => {
        const file = fileInput.files[0];
        if (!file) return;

        const reader = new FileReader();
        reader.onload = e => {
            const index = imageFiles.length;
            imageFiles.push(file);

            if (index === 0) {
                carouselInner.innerHTML = "";
            }

            const item = document.createElement("div");
            item.className = `carousel-item ${index === 0 ? "active" : ""}`;
            item.innerHTML = `
                <div class="position-relative">
                    <img src="${e.target.result}"
                         class="d-block w-100"
                         style="height: 600px; object-fit: cover;"
                         alt="업로드 미리보기">
                    <button type="button"
                            class="delete-carousel-btn"
                            data-index="${index}">&times;</button>
                </div>
            `;
            carouselInner.appendChild(item);
            updateCarouselButtons();

            // DOM 반영 후 해당 슬라이드로 이동
            setTimeout(() => {
                carouselInstance.to(index);
            }, 0);
        };
        reader.readAsDataURL(file);
        fileInput.value = "";
    });

    carouselInner.addEventListener("click", e => {
        if (!e.target.classList.contains("delete-carousel-btn")) return;
        const deleteIndex = +e.target.dataset.index;
        imageFiles.splice(deleteIndex, 1);

        const newIndex = deleteIndex === imageFiles.length
            ? deleteIndex - 1
            : deleteIndex;

        carouselInner.innerHTML = "";
        if (imageFiles.length === 0) {
            updateCarouselPlaceholder();
        } else {
            imageFiles.forEach((file, i) => {
                const reader = new FileReader();
                reader.onload = ev => {
                    const item = document.createElement("div");
                    item.className = `carousel-item ${i === newIndex ? "active" : ""}`;
                    item.innerHTML = `
                        <div class="position-relative">
                            <img src="${ev.target.result}"
                                 class="d-block w-100"
                                 style="height: 600px; object-fit: cover;"
                                 alt="업로드 미리보기">
                            <button type="button"
                                    class="delete-carousel-btn"
                                    data-index="${i}">&times;</button>
                        </div>
                    `;
                    carouselInner.appendChild(item);
                };
                reader.readAsDataURL(file);
            });
        }
        updateCarouselButtons();
    });

    document.querySelector('.submit-btn').addEventListener('click', async () => {
        const formData = new FormData();
        const textarea = document.getElementById('feed-textarea');
        const content = textarea.value.trim();

        if (!content && imageFiles.length === 0) {
            alert("내용 또는 이미지를 입력해주세요.");
            return;
        }

        formData.append("content", content);
        imageFiles.forEach(file => formData.append("images", file));

        try {
            const response = await fetch("/feeds/", {
                method: "POST",
                headers: { Authorization: `Bearer ${token}` },
                body: formData
            });
            if (!response.ok) throw new Error("서버 오류");
            const result = await response.json();
            window.location.href = `/page/detail-feed?id=${result.data.feedId}`;
        } catch (error) {
            console.error("업로드 실패", error);
            alert("업로드 중 오류가 발생했습니다.");
        }
    });

    function updateCarouselButtons() {
        const prev = carousel.querySelector(".carousel-control-prev");
        const next = carousel.querySelector(".carousel-control-next");
        if (imageFiles.length <= 1) {
            prev.style.display = "none";
            next.style.display = "none";
        } else {
            prev.style.display = "block";
            next.style.display = "block";
        }
    }

    function updateCarouselPlaceholder() {
        carouselInner.innerHTML = `
            <div class="carousel-item active">
                <div class="w-100 h-100 d-flex justify-content-center align-items-center text-muted fs-5 placeholder-slide">
                    위 이미지 업로드를 클릭하여 업로드 해주세요.
                </div>
            </div>
        `;
    }
};