// 전화번호 포맷
function formatPhoneNumber(num) {
    return num.replace(/(\d{3})(\d{3,4})(\d{4})/, "$1-$2-$3");
}

// 시간 포맷 (yyyy.mm.dd hh:mm:ss)
function formatDateTime(dateString) {
    const date = new Date(dateString);
    return `${date.getFullYear()}.${(date.getMonth() + 1).toString().padStart(2, '0')}.${date.getDate().toString().padStart(2, '0')} `
        + `${date.getHours().toString().padStart(2, '0')}:${date.getMinutes().toString().padStart(2, '0')}:${date.getSeconds().toString().padStart(2, '0')}`;
}

// 상태 포맷
function formatStatus(statusStr){
    if(statusStr == "ACTIVE") return "가입 완료";
    else if(statusStr == "INACTIVE") return "차단";
    else return "승인 대기";
}

function formatTrack(trackStr){
    return (trackStr == "BACKEND") ? "백엔드" : "프론트엔드";
}

function formatMode(modeStr){
    return (modeStr == "OFFLINE") ? "대면" : "비대면";
}

function formatBatch(batchStr){
    const batchs = {
        FIRST : "1기",
        SECOND : "2기",
        THIRD : "3기",
        FOURTH : "4기",
        FIFTH : "5기",
        SIXTH : "6기",
        SEVENTH : "7기",
        EIGHTH : "8기",
        NINTH : "9기",
        TENTH : "10기"
    }
    return batchs[batchStr];
}