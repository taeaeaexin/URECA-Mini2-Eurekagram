function makeDateStr(year, month, day, type){
    return year + type + ( (month < 10) ? '0' + month : month ) + type + ( (day < 10) ? '0' + day : day );
}

function makeTimeStr(hour, minute, second, type){
    return hour + type + ( (minute < 10) ? '0' + minute : minute ) + type + ( (second < 10) ? '0' + second : second );
}

function makePaginationHtml(listRowCount, pageLinkCount, currentPageIndex, totalListCount, htmlTargetId){
	let targetUI = document.querySelector("#" + htmlTargetId);
	
    let pageCount = Math.ceil(totalListCount/listRowCount); // 총 페이지 수
    
    let startPageIndex = 0;
    if( (currentPageIndex % pageLinkCount) == 0 ){ //10, 20...맨마지막
        startPageIndex = ((currentPageIndex / pageLinkCount)-1)*pageLinkCount + 1
    }else{
        startPageIndex = Math.floor(currentPageIndex / pageLinkCount)*pageLinkCount + 1
    }
    
    let endPageIndex = 0;
    if( (currentPageIndex % pageLinkCount) == 0 ){ //10, 20...맨마지막
        endPageIndex = ((currentPageIndex / pageLinkCount)-1)*pageLinkCount + pageLinkCount
    }else{
        endPageIndex = Math.floor(currentPageIndex / pageLinkCount)*pageLinkCount + pageLinkCount;
    }
    
    // prev 버튼 활성화 여부.
    let prev;
    if( currentPageIndex <= pageLinkCount ){
        prev = false;
    }else{
        prev = true;
    }

    // next 버튼 활성화 여부    
    let next;
    if(endPageIndex > pageCount){
        endPageIndex = pageCount
        next = false;
    }else{
        next = true;
    }
	
	// 페이지번호 ui
	let paginationHTML = `<ul class="pagination justify-content-center">`;
	
	// prev버튼 -> 이전 그룹의 마지막 페이지로 이동
	if(prev){
		paginationHTML += 
			`<li class="page-item">
		      <a class="page-link" href="javascript:movePage(${startPageIndex - 1});" aria-label="Previous">
		        <span aria-hidden="true">&laquo;</span>
		      </a>
		     </li>`;
	}
	
	// 페이지 번호 리스트. 현재페이지는 active 속성 줌.
	for(let i = startPageIndex; i<= endPageIndex; i++){
		if( i == currentPageIndex){
			paginationHTML += ` <li class="page-item active"><a class="page-link" href="javascript:movePage(${i});">${i}</a></li>`;
		} else{
			paginationHTML += ` <li class="page-item"><a class="page-link" href="javascript:movePage(${i});">${i}</a></li>`;
		}
		
	}
	
	// next버튼 -> 다음 그룹의 첫 페이지로 이동
	if(next){
		paginationHTML += 
			`<li class="page-item">
		      <a class="page-link" href="javascript:movePage(${endPageIndex + 1});" aria-label="Next">
		        <span aria-hidden="true">&raquo;</span>
		      </a>
		     </li>`;
	}
	
	paginationHTML += `</ul>`;
	
	targetUI.innerHTML = paginationHTML;
}