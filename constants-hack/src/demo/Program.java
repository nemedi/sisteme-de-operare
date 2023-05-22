package demo;

public class Program {

	public static void main(String[] args) {
		try {
			disableWarning();
			FieldSetter.set("value", Boolean.FALSE, Boolean.TRUE);
			System.out.println(Boolean.FALSE
					? "FALSE is TRUE."
					: "FALSE isn't TRUE.");
			FieldSetter.set("value", "no", "yes");
			System.out.println("no".equals("yes")
					? "no means yes."
					: "no doesn't mean yes.");
			FieldSetter.set("value", "Romania", "Hungary");
			System.out.println(String.format("Transylvania belongs to %s.", "Romania"));
		} catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
		}
	}
	
	public static void disableWarning() {
	    System.err.close();
	    System.setErr(System.out);
	}

}
