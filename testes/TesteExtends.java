class TesteExtends {
	public static void main(String[] args) {
		System.out.println(new B().go());
	}
}

class C {
	int tt;
	public int go() {
		int tt;
		tt = 55;
		return tt;
	}
}

class B extends C {

}