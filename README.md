# 빌리지(Village) - FrontEnd
- BackEnd 관련 정보는 [여기](https://github.com/minizzang/madcamp_week2_server)에서 확인해주세요.

## 앱 소개
빌리지(Village)는 

## 앱 시작



## Tab 구성
빌리지(Village)는 Home, Chatting, Request List, Profile 4개의 Tab으로 이루어져 있습니다. BottomNavigation을 활용하여 Tab을 구현했고, 아래의 Tab 버튼을 클릭하여 Tab 간 이동이 가능합니다.
> ### Home Tab
  - 앱에 등록된 지역별 아이템 목록을 보여주는 탭입니다.
    
    
    
    아이템 목록은 RecyclerView를 활용하여 구현했으며, 목록의 아이템은 Database에 저장된 아이템 이름, 대여 기간, 대여 가격을 보여줍니다. 검색창에서는 아이템 이름으로 검색이 가능하도록 했습니다. 검색창 아래의 Spinner를 이용하여 지역을 선택하면 해당 지역의 아이템 목록을 볼 수 있습니다. 또한 가격 버튼을 통해 BottomSheet을 띄웠고, 원하는 가격대를 선택하면 조건에 맞는 아이템만 목록에 표시될 수 있도록 구현했습니다.
    
    
    
    화면 아래쪽의 Floating Button을 클릭하면 새로운 아이템을 등록할 수 있는 화면이 나타납니다. 상품 이름, 대여 장소, 대여 가격, 대여 기간, 설명을 입력한 후 등록하기 Button을 클릭하면 입력한 값이 Database에 저장되고, 목록 Tab으로 돌아옵니다.
    
    
    목록의 각 아이템은 클릭이 가능하며, 클릭 시 등록할 때 저장된 아이템의 상세 정보를 DataBase에서 읽어와 확인할 수 있도록 구현했습니다. 상세 정보 창에서 채팅하기 Button 클릭 시 아이템을 등록한 사용자와의 채팅 방이 만들어져 Database에 저장되고, 채팅 창으로 이동할 수 있습니다. (채팅 관련 세부 내용은 [여기](#chatting)을 참고해주세요.) 상세 정보 창에서 빌리기 Button 클릭 시 안내창 팝업이 뜨고, 해당 아이템 빌리기 신청이 가능합니다. 빌리기 신청시 Database에 빌린 정보를 저장하도록 구현했습니다.
 
 
> ### Chatting Tab
  - 사용자가 참여한 Chatting 방의 목록을 보여주는 탭입니다.
  
  
  
    Chatting 방의 목록은 RecyclerView를 활용하여 구현했으며, Database에 저장된 사용자의 Chatting 방 목록을 읽어와 표시합니다. RecyclerView의 각 아이템을 클릭하면 해당 Chatting 방이 열리고, Chatting을 진행할 수 있습니다. 

     #### Chatting 기능 세부 내용
     


> ### Request List Tab
  - 내가 빌리기를 신청한 물건과 내가 올린 물건에 빌리기를 신청한 사람을 확인할 수 있는 Tab입니다.
  
  
  
    내가 신청한 물건은 가로 RecyclerView를 활용하여 상단에 볼 수 있도록 했습니다. Database에서 신청한 물건 정보를 가져와 각 물건의 이름, 대여자 닉네임, 대여 기간을 표시할 수 있도록 했습니다.
    
    
    내가 올린 물건에 빌리기를 신청한 사람의 목록을 중첩된 RecyclerView를 이용하여 화면에 표시하도록 했습니다. 가로 RecyclerView에 내가 올린 물건의 목록을 볼 수 있도록 했습니다. 각 물건에 세로 RecyclerView를 이용하여 해당 물건을 신청한 사람 목록을 Database에서 불러와 닉네임을 보여줄 수 있도록 구현했습니다. 신청자 닉네임 옆의 수락하기 Button 클릭 시

