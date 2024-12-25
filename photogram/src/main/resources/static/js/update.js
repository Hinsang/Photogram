// (1) 회원정보 수정
function update(userId, event) {
	event.preventDefault(); // 폼 태그 액션 막기
	// JQuery에서 .serialize()를 하면 폼 안의 모든 정보가 담긴다.
	let data = $("#profileUpdate").serialize(); // key=value 형식, formData는 serialize() 불가능

	console.log(data);

	$.ajax({
		type: "put",
		url: `/api/user/${userId}`,
		data: data,
		contentType: "application/x-www-form-urlencoded;", // key = value 요청 타입
		dataType: "json"
	}).done(res => { // httpStatus 상태코드 200번대
		console.log("성공", res);
		location.href=`/user/${userId}`;
	}).fail(error => { // HttpStatus 상태코드 200번대가 아닐 때
		if(error.data == null) {
			alert(error.responseJSON.message)
		} else {			
			alert(JSON.stringify(error.responseJSON.data));
		}
	});

}
