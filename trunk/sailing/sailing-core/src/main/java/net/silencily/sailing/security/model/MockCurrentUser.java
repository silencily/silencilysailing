package net.silencily.sailing.security.model;

public class MockCurrentUser extends DefaultCurrentUser {
	public MockCurrentUser()
	{
		this.setUserName("admin");
		this.setUserId("8a0c65e60bb40283010bb40298090012");
		this.setDept(new Dept("0000","xxµç³§"));
		this.setEmpCd("001");
	}
}
