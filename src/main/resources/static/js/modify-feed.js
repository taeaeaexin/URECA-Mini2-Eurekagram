/* ===========================================================================
   modify‑feed.js – “피드 수정” 전용 스크립트
   (add‑feed.js 로직 + 상세조회 + PATCH + 캐러셀 루프 차단)
   ========================================================================== */
let token, feedId;
const newFiles     = [];          // 새로 업로드한 File 객체
const remainImgIds = new Set();   // 삭제하지 않을 기존 이미지 id

/* ────────────────────────────────────────────────────────────────────────── */
window.addEventListener("load", bootstrapModify);   // onload 충돌 방지
/* ────────────────────────────────────────────────────────────────────────── */
async function bootstrapModify () {
    /* 1) JWT 인증 -------------------------------------------------------- */
    token = localStorage.getItem("jwt");
    if (!token || !(await auth())) return location.replace("/");

    document.body.classList.remove("hidden");

    /* 2) DOM 캐싱 & 인스턴스 -------------------------------------------- */
    const $input    = document.getElementById("image-upload");
    const $inner    = document.getElementById("carousel-inner");
    const $carEl    = document.getElementById("carouselExample");
    const carousel  = bootstrap.Carousel.getOrCreateInstance(
        $carEl, { interval:false, wrap:false }                // ← 루프 차단
    );
    const $prev     = $carEl.querySelector(".carousel-control-prev");
    const $next     = $carEl.querySelector(".carousel-control-next");

    const $textarea = document.getElementById("feed-textarea");
    const $txtPH    = document.getElementById("textarea-placeholder");

    const $submit   = document.getElementById("submit-btn");
    if (!$submit) {
        console.error('[modify-feed]  id="submit-btn" 버튼이 없습니다.');
        return;
    }

    /* 3) 이벤트 바인딩 ---------------------------------------------------- */
    $input  .addEventListener("change",  addFiles);
    $inner  .addEventListener("click",   removeSlide);
    $carEl  .addEventListener("slid.bs.carousel", updateNav);
    $submit .addEventListener("click",   patchFeed);
    $textarea.addEventListener("input",
        () => { $txtPH.style.display = $textarea.value.trim() ? "none":"block"; });

    /* 4) 상세조회 -------------------------------------------------------- */
    feedId = new URLSearchParams(location.search).get("id");
    if (!feedId) return alert("잘못된 접근입니다.");
    await loadDetail();

    /* ---------- 내부 함수 ---------- */

    /* 인증 */
    async function auth () {
        try {
            const r = await fetch("/page/authenticate",
                {method:"POST", headers:{Authorization:`Bearer ${token}` }});
            return r.ok;
        } catch { return false; }
    }

    /* 상세조회 & 화면 채우기 */
    async function loadDetail () {
        try {
            const r = await fetch(`/feeds/${feedId}`, {
                headers:{ Authorization:`Bearer ${token}` }
            });

            /* --- ▼ 디버그용 로그 추가 ------------------------ */
            console.log("[loadDetail] status =", r.status);
            if (!r.ok) {
                const msg = await r.text().catch(()=>"(본문 없음)");
                console.error("[loadDetail] response text →", msg);
                throw new Error("fetch not ok");
            }
            /* ----------------------------------------------- */

            const { data:f } = await r.json();

            // 본문 채우기
            $textarea.value      = f.content;
            $txtPH.style.display = f.content ? "none" : "block";

            // 이미지 채우기
            $inner.innerHTML = "";
            if (f.imageDtoList?.length) {
                f.imageDtoList.forEach(({ imageId, storedImageName }, idx) => {
                    remainImgIds.add(String(imageId));
                    appendSlide(`/images/${storedImageName}`, idx, imageId);
                });
            } else {
                setPlaceholder();
            }
            updateNav();
        } catch (err) {
            console.error("[loadDetail] error →", err);
            alert("피드를 불러오지 못했습니다.");
            // location.replace("/page/main");
        }
    }

    /* 파일 추가 */
    function addFiles () {
        [...$input.files].forEach(file => {
            if (!slideCount()) $inner.innerHTML = "";   // placeholder 제거

            const rdr = new FileReader();
            rdr.onload = e => {
                const idx = slideCount();               // 현재 슬라이드 개수
                newFiles.push(file);
                appendSlide(e.target.result, idx);      // 새 이미지
                updateNav();
                setTimeout(() => carousel.to(idx), 0);  // 방금 추가한 슬라이드로 이동
            };
            rdr.readAsDataURL(file);
        });
        $input.value = "";      // 동일 파일 재선택 가능
    }

    /* ─ 슬라이드 삭제 (X 버튼) */
    function removeSlide (e) {
        if (!e.target.classList.contains("delete-carousel-btn")) return;

        const slideList  = [...$inner.children];
        const slide      = e.target.closest(".carousel-item");
        const delIndex   = slideList.indexOf(slide);
        const oldId      = e.target.dataset.oldId;

        /* 1) 상태·배열 갱신 */
        slide.remove();
        oldId ? remainImgIds.delete(oldId)
            : newFiles.splice(delIndex, 1);

        /* 2) active 재지정 */
        const rest = [...$inner.children];          // 삭제 후 남은 슬라이드
        if (rest.length) {
            // 삭제 전 index 와 같은 자리(= 다음 슬라이드) 우선,
            // 없으면 이전 슬라이드
            const nextIdx = Math.min(delIndex, rest.length - 1);
            rest[nextIdx].classList.add("active");
        } else {
            setPlaceholder();                       // 모두 지워졌으면 placeholder
        }

        updateNav();
    }

    /* PATCH (수정) */
    async function patchFeed () {
        const content = $textarea.value.trim();
        if (!content) return alert("본문을 입력해주세요.");

        const fd = new FormData();
        fd.append("id", feedId);
        fd.append("content", content);
        remainImgIds.forEach(id => fd.append("remainImageIds", id));
        newFiles.forEach(f => fd.append("images", f));

        try {
            const r = await fetch(`/feeds/${feedId}`, {
                method : "PATCH",
                headers: { Authorization:`Bearer ${token}` },
                body   : fd
            });
            if (!r.ok) throw new Error("PATCH 실패");
            location.replace(`/page/detail-feed?id=${feedId}`);
        } catch (err) {
            console.error("[patchFeed] error →", err);
            alert("수정에 실패했습니다.");
        }
    }

    /* util : 현재 이미지 슬라이드 개수 */
    function slideCount () {
        return [...$inner.children]
            .filter(el => !el.querySelector(".placeholder-slide")).length;
    }

    /* util : 슬라이드 DOM 생성 */
    function appendSlide (src, idx = 0, oldId) {
        const item = document.createElement("div");
        item.className = `carousel-item${idx ? "" : " active"}`;
        item.innerHTML = `
      <div class="position-relative">
        <img src="${src}" class="d-block w-100"
             style="height:600px;object-fit:cover;">
        <button type="button" class="delete-carousel-btn"
          ${oldId ? `data-old-id="${oldId}"` : ""}>&times;</button>
      </div>`;
        $inner.appendChild(item);
    }

    /* util : placeholder 한 슬라이드만 표시 */
    function setPlaceholder () {
        $inner.innerHTML = `
      <div class="carousel-item active">
        <div class="w-100 h-100 d-flex justify-content-center align-items-center
                    text-muted fs-5 placeholder-slide">
          위 이미지 업로드를 클릭하여 이미지를 바꿔주세요.
        </div>
      </div>`;
        carousel.pause();
    }

    /* util : Prev / Next 버튼 표시 & 활성/비활성 */
    function updateNav () {
        const cnt  = slideCount();
        const actI = [...$inner.children]
            .findIndex(el => el.classList.contains("active"));

        if (cnt <= 1) {
            $prev.style.display = $next.style.display = "none";
        } else {
            $prev.style.display = $next.style.display = "block";
            $prev.disabled = (actI === 0);
            $next.disabled = (actI === cnt - 1);
        }
    }
}

/* 로그아웃 (공통) */
function logout () {
    localStorage.removeItem("jwt");
    location.replace("/");
}