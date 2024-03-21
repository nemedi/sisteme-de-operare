package rpc;

import java.io.Serializable;

class RpcResponse implements Serializable {

	private static final long serialVersionUID = 1L;
	private Object result;
	private String fault;
	private String session;

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	public String getFault() {
		return fault;
	}

	public void setFault(String fault) {
		this.fault = fault;
	}

	public String getSession() {
		return session;
	}

	public void setSession(String session) {
		this.session = session;
	}

}
