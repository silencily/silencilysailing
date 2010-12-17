<%@ taglib uri="/WEB-INF/tld/webwork.tld" prefix="ww"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>

<%-- ActionError Messages - usually set in Actions --%>
<ww:if test="hasActionErrors()">
    <div class="error fade-ffff00" id="errorMessages">	
      <ww:iterator value="actionErrors">
			    <img src="<c:url value="/images/iconWarning.gif"/>"  alt="<fmt:message key="icon.warning"/>" class="icon" />
          <font color="#FF0000"><ww:property/></font><br />
      </ww:iterator>
   </div>
</ww:if>

<%-- FieldError Messages - usually set by validation rules --%>
<!--
<ww:if test="hasFieldErrors()">
    <div class="error fade-ffff00" id="errorMessages">	
      <ww:iterator value="fieldErrors">
          <ww:iterator value="value">
						<img src="<c:url value="/images/iconWarning.gif"/>"  alt="<fmt:message key="icon.warning"/>" class="icon" />
             <font color="#FF0000"><b><ww:property/></b></font><br />
          </ww:iterator>
      </ww:iterator>
   </div>
</ww:if>
-->


<%-- Success Messages  --%>
<ww:if test="hasActionMessages()">
    <div class="message fade-ffff00" id="successMessages">	
      <ww:iterator value="actionMessages">      
				<img src="<c:url value="/images/iconInformation.gif"/>"  alt="<fmt:message key="icon.information"/>" class="icon" />
				<font color="#339933"><ww:property/></font><br />
      </ww:iterator>
   </div>
</ww:if>

<ww:if test="#session[@vs@SESSION_OPERATE_MESSAGE] != null">
	 <div class="message" id="successMessages">	
        <div class="update_right"><ww:property value="#session.remove(@vs@SESSION_OPERATE_MESSAGE)"/></div>
    </div>
	 <%--
	 <script>
	 	messageBox("<ww:property value="#session.remove(@vs@SESSION_OPERATE_MESSAGE)"/>");
		function messageBox(message)
		{
			showModalDialog('<ww:url value = "'/common/successMessage.jsp'" />', message, 'dialogWidth:600px;dialogHeight:300px;status:no;help:no;resizable:no;scroll:no');
		}
	 </script>
	 --%>
</ww:if>

