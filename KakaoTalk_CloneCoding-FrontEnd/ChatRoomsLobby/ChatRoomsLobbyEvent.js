window.onload = function() {
    //친구로비 이동 이벤트
    var userElement = document.getElementsByClassName("menu user")[0];
    userElement.addEventListener("click", moveChatLobbyEvent, false);
    function moveChatLobbyEvent(e) {
        location.replace("../FriendsLobby/FriendsLobby.html");
    }

    //설정창 이동 이벤트
    var settingElement = document.getElementsByClassName("menu dots")[0];
    settingElement.addEventListener("click", moveSettingEvent, false);
    function moveSettingEvent(e) {
        location.replace("../Setting/Setting.html");
    }

    //채팅박스 추가
    addChatBoxWithCY();

    
    //정렬순서창 숨기기 이벤트
    var organizeDivElement = document.getElementById("organizeMenu");
    organizeDivElement.style.display = "none";
    organizeDivElement.addEventListener("click", hideOrgaMenu, false);
    function hideOrgaMenu(e) {
        organizeDivElement.style.display = "none";
    }
    
    //정렬순서창 보이기 이벤트
    var showOrgaBtnElement = document.getElementsByClassName("showOrganize");
    showOrgaBtnElement[0].addEventListener("click", showOrganizeMenuEvent, false);
    showOrgaBtnElement[1].addEventListener("click", showOrganizeMenuEvent, false);
    function showOrganizeMenuEvent(e) {
        organizeDivElement.style.display = "block";
    }
    
    
    //새로운 채팅방 생성 이벤트
    var addNewChatBoxElement = document.getElementsByClassName("header addChat")[0];
    addNewChatBoxElement.addEventListener("click", addChatBoxWithCY, false);
}

function addChatBoxWithCY() {
    var name = "나채영";
    var profile = "../images/heart.png";
    var content = "태균아 좀있다가 보자!! 사랑행♥";
    
    //요소 생성
    var chatBox = document.createElement("div");
    var chatProfile = document.createElement("img");
    var chatProfileDiv = document.createElement("div");
    var chatName = document.createElement("p");
    var chatContent = document.createElement("p");
    var chatTime = document.createElement("p");
    var chatTitle = document.createElement("div");

    //요소 설정
    chatProfile.setAttribute("src", profile);
    chatName.innerHTML = name;
    chatContent.innerHTML = content;
    chatTime.innerHTML = "오후 6:22";

    //요소 결합
    chatProfileDiv.appendChild(chatProfile);
    chatTitle.appendChild(chatName);
    chatTitle.appendChild(chatTime);

    chatBox.appendChild(chatProfileDiv);
    chatBox.appendChild(chatTitle);
    chatBox.appendChild(chatContent);

    //요소 스타일 적용
    setChatBoxStyle(chatBox);
    
    //채팅박스 추가
    var chatListElement = document.getElementById("chatList")
    chatListElement.append(chatBox); 
}

function setChatBoxStyle(chatBox) {
    var profileDiv = chatBox.children[0];
    var title = chatBox.children[1];
    var name = title.children[0];
    var time = title.children[1];
    var content = chatBox.children[2];

    chatBox.style.height = "80px";


    profileDiv.style.width = "50px";
    profileDiv.style.height = "50px";
    profileDiv.style.float = "left";
    profileDiv.style.display = "block";
    profileDiv.style.borderRadius = "30%";
    profileDiv.style.overflow = "hidden";
    profileDiv.style.marginRight = "20px";
    
    profileDiv.children[0].style.width = "100%";
    profileDiv.children[0].style.height = "100%";
    profileDiv.children[0].style.objectFit = "cover";
    
    title.style.height = "27px";
    title.style.width = "340px";
    title.style.display = "inline-block";
    
    name.style.display = "inline-block";
    name.style.float = "left";
    name.style.fontWeight = "bold";
    name.style.fontSize = "large";
    name.style.margin = "0";
    name.style.marginBottom = "5px";
    
    time.style.display = "inline-block";
    time.style.float = "right";
    time.style.color = "gray";
    time.style.fontSize = "smaller";
    time.style.margin = "0";
    time.style.marginTop = "5px";
    
    content.style.display = "inline";
    content.style.color = "darkgray";
    content.style.float = "left";
    content.style.marginTop = "0";
    content.style.width = "300px";
    content.style.textOverflow = "ellipse";
}