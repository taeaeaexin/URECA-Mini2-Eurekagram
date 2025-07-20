// 취소 (로그인 화면으로 이동)
function back() {
    window.location.href = "/";
}

// 회원가입 요청
async function join() {
    const userName = document.getElementById("userName").value;
    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;
    const passwordcheck = document.getElementById("passwordcheck").value;
    const nickName = document.getElementById("nickName").value;
    const phoneNumber = document.getElementById("phoneNumber").value;
    const track = document.getElementById("track").value;
    const mode = document.getElementById("mode").value;
    const batch = document.getElementById("batch").value;

    if (!validateUserName(userName)) return;
    if (!validateEmail(email)) return;
    if (!validatePassword(password)) return;
    if (!validatePasswordCheck(passwordcheck)) return;
    if (!validateNickName(nickName)) return;
    if (!validatePhoneNumber(phoneNumber)) return;

    const response = await fetch("/users", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({ userName, email, password, nickName, phoneNumber, track, mode, batch })
    });

    const result = await response.json();

    if (result.statusCode === 200) {
        window.location.href = "/";
    } else {
        alert(result.message);
    }
}

// 유효성 검사 함수들
function validateUserName(val) {
    const msg = document.getElementById("userNameMessage");
    if (!val) {
        msg.textContent = "이름을 입력하세요.";
        return false;
    }
    msg.textContent = "";
    return true;
}

function validateEmail(val) {
    const msg = document.getElementById("emailMessage");
    if (!val) {
        msg.textContent = "이메일을 입력하세요.";
        return false;
    }
    msg.textContent = "";
    return true;
}

function validatePassword(val) {
    const msg = document.getElementById("passwordMessage");
    if (!val) {
        msg.textContent = "비밀번호를 입력하세요.";
        return false;
    }
    msg.textContent = "";
    return true;
}

function validatePasswordCheck(val) {
    const pw = document.getElementById("password").value;
    const msg = document.getElementById("passwordCheckMessage");
    if (val !== pw) {
        msg.textContent = "비밀번호가 일치하지 않습니다.";
        return false;
    }
    msg.textContent = "";
    return true;
}

function validateNickName(val) {
    const msg = document.getElementById("nickNameMessage");
    if (!val) {
        msg.textContent = "닉네임을 입력하세요.";
        return false;
    }
    msg.textContent = "";
    return true;
}

function validatePhoneNumber(val) {
    const msg = document.getElementById("phoneNumberMessage");
    if (!val) {
        msg.textContent = "전화번호를 입력하세요.";
        return false;
    }
    msg.textContent = "";
    return true;
}