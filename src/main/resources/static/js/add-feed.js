/* ===========================================================================
   add‑feed.js  –  “피드 작성” 전용 스크립트
   (무한 루프 차단 + 슬라이드 삭제 시 올바른 인덱스 유지)
   ========================================================================== */
let token;

/* ── 0) JWT 인증 후 화면 노출 ─────────────────────────────────────────────── */
(async () => {
    token = sessionStorage.getItem("jwt");
    if (!token) return location.replace("/");

    const ok = await fetch("/page/authenticate", {
        method : "POST",
        headers: { Authorization: `Bearer ${token}` }
    }).then(r => r.ok).catch(()=>false);

    if (!ok) return location.replace("/");
    document.body.classList.remove("hidden");
})();

/* ─────────────────────────────────────────────────────────────────────────── */
window.addEventListener("load", () => {
    /* --- 캐싱 & 인스턴스 ---------------------------------------------------- */
    const $fileInput     = document.getElementById("image-upload");
    const $inner         = document.getElementById("carousel-inner");
    const $carEl         = document.getElementById("carouselExample");
    const carousel       = bootstrap.Carousel.getOrCreateInstance($carEl,
        { interval:false, wrap:false });  // ◎ wrap false
    const $prevBtn       = $carEl.querySelector(".carousel-control-prev");
    const $nextBtn       = $carEl.querySelector(".carousel-control-next");
    const $textarea      = document.getElementById("feed-textarea");

    const imageFiles = [];   // File 객체 배열

    /* ---------- placeholder 최초 세팅 ---------- */
    drawPlaceholder();

    /* ---------- 이벤트 ---------- */
    $fileInput.addEventListener("change", addFiles);
    $inner     .addEventListener("click",  deleteSlide);
    $carEl     .addEventListener("slid.bs.carousel", updateNav);
    document.querySelector(".submit-btn")
        .addEventListener("click", submitFeed);

    /* ---------- 파일 선택 ---------- */
    function addFiles(){
        [...$fileInput.files].forEach(file=>{
            // placeholder 있었으면 제거
            if (!slideCount()) $inner.innerHTML = "";

            const idx = imageFiles.length;   // 추가될 인덱스
            imageFiles.push(file);

            appendSlide(URL.createObjectURL(file), idx); // 새 슬라이드
            rebuildSlides(idx);                          // 인덱스 맞춰 다시 구성
        });
        $fileInput.value = "";   // 같은 파일 다시 선택 가능
    }

    /* ---------- 슬라이드 삭제 ---------- */
    function deleteSlide(e){
        if (!e.target.classList.contains("delete-carousel-btn")) return;

        const slide = e.target.closest(".carousel-item");
        const idx   = +e.target.dataset.index;   // data-index 로 저장해둔 값

        imageFiles.splice(idx,1);                // 배열에서 제거
        rebuildSlides(idx === imageFiles.length ? idx-1 : idx); // 재구성
    }

    /* ---------- 실제 업로드 ---------- */
    async function submitFeed(){
        const content = $textarea.value.trim();
        if (!content && imageFiles.length===0){
            return alert("내용 또는 이미지를 입력해주세요.");
        }

        const fd = new FormData();
        fd.append("content", content);
        imageFiles.forEach(f => fd.append("images", f));

        try{
            const r = await fetch("/feeds/", {
                method : "POST",
                headers: { Authorization:`Bearer ${token}` },
                body   : fd
            });
            if(!r.ok) throw 0;
            const {data:{feedId}} = await r.json();
            location.replace(`/page/detail-feed?id=${feedId}`);
        }catch{
            alert("업로드 중 오류가 발생했습니다.");
        }
    }

    /* ====================================================================== */
    /* ---------------- util ---------------- */
    function slideCount(){
        return imageFiles.length;
    }

    /* 새 slide DOM 한 개 생성 */
    function appendSlide(src, idx){
        $inner.insertAdjacentHTML("beforeend", slideHTML(src,idx,false));
    }

    /* 인덱스(activeIdx)를 지정해서 캐러셀을 **한 방에** 다시 그림 */
    function rebuildSlides(activeIdx=0){
        if (imageFiles.length===0){
            drawPlaceholder();
            updateNav();
            return;
        }
        let html="";
        imageFiles.forEach((file,i)=>{
            const src = URL.createObjectURL(file);
            html += slideHTML(src,i, i===activeIdx);
        });
        $inner.innerHTML = html;
        carousel.to(activeIdx);
        updateNav();
    }

    /* Prev/Next 버튼 상태 및 표시 제어 */
    function updateNav(){
        const cnt     = slideCount();
        const actIdx  = [...$inner.children]
            .findIndex(el=>el.classList.contains("active"));

        if (cnt<=1){
            $prevBtn.style.display = $nextBtn.style.display = "none";
        }else{
            $prevBtn.style.display = $nextBtn.style.display = "block";
            $prevBtn.disabled = (actIdx===0);
            $nextBtn.disabled = (actIdx===cnt-1);
        }
    }

    /* placeholder 한 개만 표시 */
    function drawPlaceholder(){
        $inner.innerHTML = `
      <div class="carousel-item active">
        <div class="w-100 h-100 d-flex justify-content-center align-items-center
                    text-muted fs-5 placeholder-slide">
          위 이미지 업로드를 클릭하여 업로드 해주세요.
        </div>
      </div>`;
        carousel.pause();   // 자동 이동 & 키보드 전부 비활
    }

    /* slide HTML 조립 */
    function slideHTML(src, idx, isActive){
        return `
    <div class="carousel-item${isActive?" active":""}">
      <div class="position-relative">
        <img src="${src}" class="d-block w-100"
             style="height:600px;object-fit:cover;">
        <button type="button" class="delete-carousel-btn"
                data-index="${idx}">&times;</button>
      </div>
    </div>`;
    }

    /* 로그아웃 전역 */
    window.logout = ()=>{
        sessionStorage.removeItem("jwt");
        location.replace("/");
    };
});