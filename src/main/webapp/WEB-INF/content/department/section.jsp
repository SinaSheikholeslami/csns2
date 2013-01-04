<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<c:set var="section" value="${gradeSheet.section}"/>

<script>
$(function(){
   $("table").tablesorter({
      sortList: [[1,0]]
   });
   $("#selectAll").toggle(
       function(){ $(":checkbox[name='userId']").attr("checked",true); },
       function(){ $(":checkbox[name='userId']").attr("checked",false); }
   );
   $("#email").click(function(){
       if( $(":checkbox[name='userId']:checked").length == 0 )
           alert( "Please select the student(s) to contact." );
       else
           $("#studentsForm").attr("action", "<c:url value='/email/compose' />").submit();
   });
});
</script>

<ul id="title">
<li><a class="bc" href="<c:url value='/section/search' />">Sections</a></li>
<li><a class="bc" href="<c:url value='/department/${dept}/sections?quarter=${section.quarter.code}' />">${department.name}</a>
<li>${section.course.code}, ${section.quarter}</li>
<li class="align_right"><a id="email" href="javascript:void(0)"><img title="Email Students"
    alt="[Email Student(s)]" src="<c:url value='/img/icons/email_to_friend.png' />" /></a></li>
</ul>

<p>Total Students: ${fn:length(gradeSheet.studentGrades)}</p>

<form id="studentsForm" method="post">
<table class="viewtable">
<thead>
  <tr>
    <th><input id="selectAll" type="checkbox" /></th>
    <th>Name</th><th>Grade</th>
    <c:forEach items="${section.assignments}" var="assignment">
      <th>${assignment.alias}</th>
    </c:forEach>
  </tr>
</thead>
<tbody>
  <c:forEach items="${gradeSheet.studentGrades}" var="studentGrade">
  <c:set var="enrollment" value="${studentGrade.key}" />
    <tr>
      <td class="center"><input type="checkbox" name="userId" value="${enrollment.student.id}" /></td>
      <td>${enrollment.student.lastName}, ${enrollment.student.firstName}</td>
      <td class="center">${enrollment.grade.symbol}</td>
      <c:forEach items="${studentGrade.value}" var="grade">
        <td class="center">${grade}</td>
      </c:forEach>
    </tr>
  </c:forEach>
</tbody>
</table>
<input type="hidden" name="backUrl" value="/department/${dept}/section?id=${section.id}" />
</form>
