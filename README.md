# MLServer
딥러닝 서버  
앱서버와는 별개로 딥러닝이 필요한 작업들만을 처리하는 서버  
Django 로 제작됨

# TryangleAppServer
앱과 직접적으로 통신하는 서버로 Spring 으로 제작됨  
앱에서 딥러닝을 필요로하는 기능에대한 요청이 들어오면 MLServer 로 전달하는 역할을 함  
데이터 베이스 관리 또한 앱서버에서 동작함  

# admin-frontend
주로 앱 내에 들어갈 사진과 사진의 채점을 위한 플랫폼  
추후 관리자 페이지로 확장될 수 있는 프로젝트  