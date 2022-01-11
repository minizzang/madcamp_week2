# 빌리지(Village) - Frontend
- FrondEnd : Android Studio, Kotlin
- BackEnd 관련 정보는 [여기](https://github.com/minizzang/madcamp_week2_server)에서 확인해주세요.

## 앱 소개
빌리지(Village)는 물건 공유 플랫폼입니다. 


사기에는 아깝지만 꼭 필요한 물건이 있다면 빌리지에서 물건을 빌려보세요!


여행갈 때, 짐이 너무 많다면 걱정하지 마세요! 빌리지에서 그 지역의 주민들에게 물건을 빌려보세요!

## 앱 시작

앱 시작 시 로그인 화면이 보여집니다. 네이버 로그인 SDK를 활용하여 로그인을 구현했습니다.


앱에 처음 등록하는 사용자의 경우 네이버 로그인 후 앱 내에서 사용할 닉네임과 기본 지역을 설정하여 앱에 가입할 수 있습니다. 초기 앱 가입 시 사용자의 정보를 Database에 저장하게 됩니다. 이미 등록 된 사용자는 네이버 로그인을 통해 바로 앱에 로그인을 할 수 있으며, 로그인 시 Database에서 불러온 사용자의 정보를 SharedPreference에 저장하게 됩니다.

|앱 시작 화면|로그인 화면|닉네임 설정 화면|
|:-:|:-:|:-:|
|![](https://github.com/minizzang/madcamp_week2/blob/master/Screenshots/Screenshot_20220111-202505.jpg)|![](https://github.com/minizzang/madcamp_week2/blob/master/Screenshots/Screenshot_20220111-190050.jpg)|![](https://github.com/minizzang/madcamp_week2/blob/master/Screenshots/InkedScreenshot_20220111-185816_LI.jpg)|




## Tab 구성
빌리지(Village)는 Home, Chatting, Request List, Profile 4개의 Tab으로 이루어져 있습니다. 


BottomNavigation을 활용하여 Tab을 구현했고, 아래의 Tab 버튼을 클릭하여 Tab 간 이동이 가능합니다.
> ### Home Tab
  - 앱에 등록된 지역별 아이템 목록을 보여주는 탭입니다.
    
    
    
    아이템 목록은 RecyclerView를 활용하여 구현했으며, 목록의 아이템은 Database에 저장된 아이템 이름, 대여 기간, 대여 가격을 보여줍니다. 검색창에서는 아이템 이름으로 검색이 가능하도록 했습니다. 검색창 아래의 Spinner를 이용하여 지역을 선택하면 해당 지역의 아이템 목록을 볼 수 있습니다. 또한 가격 버튼을 통해 BottomSheet을 띄웠고, 원하는 가격대를 선택하면 조건에 맞는 아이템만 목록에 표시될 수 있도록 구현했습니다.
    
    
    
    화면 아래쪽의 Floating Button을 클릭하면 새로운 아이템을 등록할 수 있는 화면이 나타납니다. 상품 이름, 대여 장소, 대여 가격, 대여 기간, 설명을 입력한 후 등록하기 Button을 클릭하면 입력한 값이 Database에 저장되고, 목록 Tab으로 돌아옵니다.
    
    
    목록의 각 아이템은 클릭이 가능하며, 클릭 시 등록할 때 저장된 아이템의 상세 정보를 DataBase에서 읽어와 확인할 수 있도록 구현했습니다. 상세 정보 창에서 채팅하기 Button 클릭 시 아이템을 등록한 사용자와의 채팅 방이 만들어져 Database에 저장되고, 채팅 창으로 이동할 수 있습니다. (채팅 관련 세부 내용은 [여기](#chatting)을 참고해주세요.) 상세 정보 창에서 빌리기 Button 클릭 시 안내창 팝업이 뜨고, 해당 아이템 빌리기 신청이 가능합니다. 빌리기 신청시 Database에 빌린 정보를 저장하도록 구현했습니다.
    
    |홈 메인 화면|가격 조건 설정|물건 등록 화면|물건 상세 정보|
    |:-:|:-:|:-:|:-:|
    |![](https://github.com/minizzang/madcamp_week2/blob/master/Screenshots/Screenshot_20220111-190211.jpg)|![](https://github.com/minizzang/madcamp_week2/blob/master/Screenshots/Screenshot_20220111-190246.jpg)|![](https://github.com/minizzang/madcamp_week2/blob/master/Screenshots/Screenshot_20220111-190238.jpg)|![](https://github.com/minizzang/madcamp_week2/blob/master/Screenshots/Screenshot_20220111-190257.jpg)|
 
 
> ### Chatting Tab
  - 사용자가 참여한 Chatting 방의 목록을 보여주는 탭입니다.
  
  
  
    Chatting 방의 목록은 RecyclerView를 활용하여 구현했으며, Database에 저장된 사용자의 Chatting 방 목록을 읽어와 표시합니다. RecyclerView의 각 아이템을 클릭하면 해당 Chatting 방이 열리고, Chatting을 진행할 수 있습니다. 
    
     #### Chatting 기능 세부 내용
     
       - 실시간 Chatting 기능은 Socket 통신을 활용하여 구현하였습니다. 또한, Chatting 내용을 Database에도 저장하여 다시 Chatting 방에 접속했을 때, 이전의 Chatting 내용을 불러와 확인할 수 있도록 했습니다. 
    
    |채팅방 목록|실시간 채팅 화면|
    |:-:|:-:|
    |![](https://github.com/minizzang/madcamp_week2/blob/master/Screenshots/Screenshot_20220111-190607.jpg)|![](https://github.com/minizzang/madcamp_week2/blob/master/Screenshots/Screenshot_20220111-190513.jpg)|



> ### Request List Tab
  - 내가 빌리기를 신청한 물건과 내가 올린 물건에 빌리기를 신청한 사람을 확인할 수 있는 Tab입니다.
  
  
  
    내가 신청한 물건은 가로 RecyclerView를 활용하여 상단에 볼 수 있도록 했습니다. Database에서 신청한 물건 정보를 가져와 각 물건의 이름, 대여자 닉네임, 대여 기간을 표시할 수 있도록 했습니다.
    
    
    내가 올린 물건에 빌리기를 신청한 사람의 목록을 중첩된 RecyclerView를 이용하여 화면에 표시하도록 했습니다. 가로 RecyclerView에 내가 올린 물건의 목록을 볼 수 있도록 했습니다. 각 물건에 세로 RecyclerView를 이용하여 해당 물건을 신청한 사람 목록을 Database에서 불러와 닉네임을 보여줄 수 있도록 구현했습니다. 신청자 닉네임 옆의 수락하기 Button 클릭 시 신청자와의 물건 빌리기가 체결되며 해당 물건은 물건 목록에서 사라지게 됩니다.
    
    |신청 물건 목록|
    |:-:|
    |<img src="https://github.com/minizzang/madcamp_week2/blob/master/Screenshots/Screenshot_20220111-190625.jpg" width="400"/>|
    
> ### Profile Tab
  - 로그인 한 사용자의 정보를 보여주는 Tab입니다.
  
  
  
    앱에 로그인 시 SharedPreferences로 사용자 정보를 저장하며, 저장된 정보를 화면에 띄워줍니다. 하단에 위치한 로그아웃 버튼 클릭 시 앱에서 로그아웃이 가능합니다. 로그아웃 시 기기의 내부 저장소에 SharedPreferences로 저장된 사용자의 정보가 삭제됩니다.
    
    
    상단에 이력보기 버튼을 클릭하면, 나의 빌리기 이력을 확인할 수 있는 창으로 연결됩니다. 나의 빌리기 이력 창은 빌리기가 체결된 빌려준 물건 이력과 빌린 물건 이력을 Database에서 읽어와 표시합니다. 각각 가로 RecyclerView로 구성했으며, 물건 이름, 상대방 닉네임, 대여 기간을 표시해줍니다.
    
    |사용자 프로필|빌리기 이력|
    |:-:|:-:|
    |![](https://github.com/minizzang/madcamp_week2/blob/master/Screenshots/InkedScreenshot_20220111-190640_LI.jpg)|![](https://github.com/minizzang/madcamp_week2/blob/master/Screenshots/Screenshot_20220111-190700.jpg)|


## Credit
- 김민희 ([minizzang](https://github.com/minizzang))
- 조민서 ([jjminsuh](https://github.com/jjminsuh))
