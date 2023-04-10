window.onload = function() {
    //json파일 가져오기
    function getUserInfoJson() {
        var req = new XMLHttpRequest();
        var userInfo;

        req.onreadystatechange = function() {
            if(req.readyState == 4) {
                if(req.status == 200) {
                    userInfo = JSON.parse(req.responseText);
                }
            }
        }

        req.open("GET", "../userInfo.json", false);
        req.send(null);
        return userInfo;
    }

    //json파일 수정
    function setNewPW(newPW) {
        
    }


    var currentPW = document.getElementById("currentPW");
    var newPW = document.getElementById("newPW");
    var confirmPW = document.getElementById("confirmPW");
    var processBtn = document.getElementById("processBtn");

    processBtn.addEventListener("click", changePWEvent);

    function changePWEvent(e) {
        var userInfo = getUserInfoJson();
        if(currentPW.value == userInfo.password) {
            if(newPW.value == confirmPW.value) {
                //유저 JSON파일 수정
                alert("일시적으로 수정이 불가능합니다.");
            } else {
                alert("새 비밀번호와 비밀번호 확인이 일치하지 않습니다.");
            }
        } else {
            alert("현재 비밀번호가 틀렸습니다.");
        }
    }

};