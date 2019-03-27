package edu.uoc.elc.lti.tool;

import edu.uoc.elc.lti.exception.InvalidTokenException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
public class ToolTest {
	private Tool sut;

	@Before
	public void setUp() {
		this.sut = new Tool(
						"Universitat Oberta de Catalunya",
						"Universitat Oberta de Catalunya",
						"https://lti-ri.imsglobal.org/platforms/68/platform_keys/60.json",
						"https://lti-ri.imsglobal.org/platforms/68/access_tokens",
						"MIIEpQIBAAKCAQEAwDN0DFZnsxETm+lzQh7dsbBX6srNGqQ+Ougc485pqys81TzjoWDsubj70NdTge8DO3ycJbPpDsTH5P1Ea+vCHq/h1SzFLLF0yLw5Y2sWrLlDF9PrvR9yApu0bZ3zNTl55B4EblYns3j3JydwfnnLuNeNVpwv7wosLZKhMvg9CQv39prJj8xI+l5FoRkhKS86tl5PrU4Xld+jBo84GWAx0SYxbGF/vR9ve9lnErNWLv8ozYC2J9FusberZrsZ8M0mpNR1vJMayqDIwMPX5rsKtWZJh33XwAKRsEIxWT0WUGM54SUc0jrszapXfOoznblwmcAd/MVLXOlgOoaeiQbxFwIDAQABAoIBAQCV6UwRt29I1v/xcuPoZcTLLF1Wj/nsVsEHFzY5ZS08Sgw30jdGDyB1N9iJqvnCddeSlX4BVlNopom3jOfKOWf+oXbG3BWq53rMeytT5RoZfKLdOfk8WQOHD0Vq3nDtSbhN+i/Ak8iBjs9Ppc5zh7IjqVTfghFNJCUTIrRC2iZZH7b+deK4vWd6GnIubftHs3H/worivZFPXm+qG0+mSLo9DdxuySQmG0KwwJOz/RwW7oKME0Y05+eUA0E2EUODEzf/iTBsnZxDJ1PVuJvWiSl4uDh167G4mnscPiLYcDc4xJ4rmr+mDoekWMn70iNzW4xLZQaLMLy/RtKoclFVDQZxAoGBAOxQJOm96wBVT+zfaO/gork6DwOFxdf765C8sNsRSwygzrh03UYbO0bFwi2r2/bBXvyt69DGxKapUhDH0z5inVjsZwHAdrHAGpf+jA9smumsJI25/AXmp9piHSx1SI5AjygViCETSD6ItWhTbEBJn0rwhAs+f8hFGymwF11T4HH/AoGBANA2h5O7vlYGHmgEVyol/oRiXrggx2z0iVkn3RUhzWqXqedaQjp8zyA+FG/7fPsYah9R1sgKZ2aey+npo7RKQgmsgJWTQsXq+Fcvivw1z0xIJ8AmK4lhTHlw/B9TT3cxzy1IqAUfP4luJXnsiZ45G78XD6Q5Ftd8Fxn+PhDq4NDpAoGBAIClyNKvH6ZYy2Aq59ffNPcdklrakrBYZw+uiaFZMsA3MxLcHDI0VPrcYi+25dLZxrpMfJp1+0y31QNppajKytpEKHedrYBrEo84dktXVqZrnqLBY2BbB3ot+6/eUZePsd+iiS9obeYNSqT29XGyItQLR/dPGQWQCY+SW8XlCcVFAoGBAMOP5AmDVkPQHXEPWptQ8lx/VH3W89jHWdXulj2J8TlD2CZfZUMwBQ9An8uKR5pEFTDzmitrcjE1x0sd0k+9S4dwiZlzpkzk5HpnQkCffeQlBYj5kPzI8Z5C29vEUSggFXpv+rhM4E2BshtxatS8yO3TiDJ0GJsuhzg3zy3unlg5AoGAZv9YjAoQdxrRl1AiWntZm8BXxXwXEVEG2Wbr9a6+OPiaQXgpk11nf2QnEPmFZ2vFSC6YWEba34qs3kNBg5hp0ttHNyzwNxL7B+4sRoN08lVtOYUOGeqg3yGkdQCxW4RMiAL37+01rrbSGUxieWjP3V4y5gP2//mnlan/s4lbkOc=",
						"MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwDN0DFZnsxETm+lzQh7dsbBX6srNGqQ+Ougc485pqys81TzjoWDsubj70NdTge8DO3ycJbPpDsTH5P1Ea+vCHq/h1SzFLLF0yLw5Y2sWrLlDF9PrvR9yApu0bZ3zNTl55B4EblYns3j3JydwfnnLuNeNVpwv7wosLZKhMvg9CQv39prJj8xI+l5FoRkhKS86tl5PrU4Xld+jBo84GWAx0SYxbGF/vR9ve9lnErNWLv8ozYC2J9FusberZrsZ8M0mpNR1vJMayqDIwMPX5rsKtWZJh33XwAKRsEIxWT0WUGM54SUc0jrszapXfOoznblwmcAd/MVLXOlgOoaeiQbxFwIDAQAB"
		);
	}

	@Test
	public void decode() throws InvalidTokenException {
		String token = "eyJhbGciOiJSUzI1NiIsImtpZCI6InBVYUFkb2VmQ2Q1VGctVEM4MDdtalJlSGpTM2VjOG5zWTktbnJwV0RRUzAifQ.eyJodHRwczovL3B1cmwuaW1zZ2xvYmFsLm9yZy9zcGVjL2x0aS9jbGFpbS9tZXNzYWdlX3R5cGUiOiJMdGlSZXNvdXJjZUxpbmtSZXF1ZXN0IiwiZ2l2ZW5fbmFtZSI6IkVsZG9uIiwiZmFtaWx5X25hbWUiOiJIb3BwZSIsIm1pZGRsZV9uYW1lIjoiUm9kbmV5IiwicGljdHVyZSI6Imh0dHA6Ly9leGFtcGxlLm9yZy9FbGRvbi5qcGciLCJlbWFpbCI6IkVsZG9uLkhvcHBlQGV4YW1wbGUub3JnIiwibmFtZSI6IkVsZG9uIFJvZG5leSBIb3BwZSIsImh0dHBzOi8vcHVybC5pbXNnbG9iYWwub3JnL3NwZWMvbHRpL2NsYWltL3JvbGVzIjpbImh0dHA6Ly9wdXJsLmltc2dsb2JhbC5vcmcvdm9jYWIvbGlzL3YyL21lbWJlcnNoaXAjTGVhcm5lciIsImh0dHA6Ly9wdXJsLmltc2dsb2JhbC5vcmcvdm9jYWIvbGlzL3YyL2luc3RpdHV0aW9uL3BlcnNvbiNTdHVkZW50IiwiaHR0cDovL3B1cmwuaW1zZ2xvYmFsLm9yZy92b2NhYi9saXMvdjIvbWVtYmVyc2hpcCNNZW50b3IiXSwiaHR0cHM6Ly9wdXJsLmltc2dsb2JhbC5vcmcvc3BlYy9sdGkvY2xhaW0vcm9sZV9zY29wZV9tZW50b3IiOlsiYTYyYzUyYzAyYmEyNjIwMDNmNWUiXSwiaHR0cHM6Ly9wdXJsLmltc2dsb2JhbC5vcmcvc3BlYy9sdGkvY2xhaW0vcmVzb3VyY2VfbGluayI6eyJpZCI6IjMxOSIsInRpdGxlIjoiVGVzdCBUb29sIiwiZGVzY3JpcHRpb24iOiJMYXVuY2ggdGVzdCB0b29sIn0sImh0dHBzOi8vcHVybC5pbXNnbG9iYWwub3JnL3NwZWMvbHRpL2NsYWltL2NvbnRleHQiOnsiaWQiOiI4OCIsImxhYmVsIjoiVGVzdCBDb3Vyc2UiLCJ0aXRsZSI6IlRlc3QgQ291cnNlIiwidHlwZSI6WyJDb3Vyc2VPZmZlcmluZyJdfSwiaHR0cHM6Ly9wdXJsLmltc2dsb2JhbC5vcmcvc3BlYy9sdGkvY2xhaW0vdG9vbF9wbGF0Zm9ybSI6eyJuYW1lIjoiVU9DIFRlc3QgcGxhdGZvcm0iLCJjb250YWN0X2VtYWlsIjoiIiwiZGVzY3JpcHRpb24iOiIiLCJ1cmwiOiIiLCJwcm9kdWN0X2ZhbWlseV9jb2RlIjoiIiwidmVyc2lvbiI6IjEuMCJ9LCJodHRwczovL3B1cmwuaW1zZ2xvYmFsLm9yZy9zcGVjL2x0aS1hZ3MvY2xhaW0vZW5kcG9pbnQiOnsic2NvcGUiOlsiaHR0cHM6Ly9wdXJsLmltc2dsb2JhbC5vcmcvc3BlYy9sdGktYWdzL3Njb3BlL2xpbmVpdGVtIiwiaHR0cHM6Ly9wdXJsLmltc2dsb2JhbC5vcmcvc3BlYy9sdGktYWdzL3Njb3BlL3Jlc3VsdC5yZWFkb25seSIsImh0dHBzOi8vcHVybC5pbXNnbG9iYWwub3JnL3NwZWMvbHRpLWFncy9zY29wZS9zY29yZSJdLCJsaW5laXRlbXMiOiJodHRwczovL2x0aS1yaS5pbXNnbG9iYWwub3JnL3BsYXRmb3Jtcy82OC9jb250ZXh0cy84OC9saW5lX2l0ZW1zIn0sImh0dHBzOi8vcHVybC5pbXNnbG9iYWwub3JnL3NwZWMvbHRpLW5ycHMvY2xhaW0vbmFtZXNyb2xlc2VydmljZSI6eyJjb250ZXh0X21lbWJlcnNoaXBzX3VybCI6Imh0dHBzOi8vbHRpLXJpLmltc2dsb2JhbC5vcmcvcGxhdGZvcm1zLzY4L2NvbnRleHRzLzg4L21lbWJlcnNoaXBzLmpzb24iLCJzZXJ2aWNlX3ZlcnNpb24iOiIyLjAifSwiaHR0cHM6Ly9wdXJsLmltc2dsb2JhbC5vcmcvc3BlYy9sdGktY2VzL2NsYWltL2NhbGlwZXItZW5kcG9pbnQtc2VydmljZSI6eyJzY29wZXMiOlsiaHR0cHM6Ly9wdXJsLmltc2dsb2JhbC5vcmcvc3BlYy9sdGktY2VzL3YxcDAvc2NvcGUvc2VuZCJdLCJjYWxpcGVyX2VuZHBvaW50X3VybCI6Imh0dHBzOi8vbHRpLXJpLmltc2dsb2JhbC5vcmcvcGxhdGZvcm1zLzY4L3NlbnNvcnMiLCJjYWxpcGVyX2ZlZGVyYXRlZF9zZXNzaW9uX2lkIjoidXJuOnV1aWQ6OTgyMDJhYzdmYTAxOWVmMDAwNWQifSwiaXNzIjoiaHR0cHM6Ly93d3cudW9jLmVkdSIsImF1ZCI6IlVuaXZlcnNpdGF0IE9iZXJ0YSBkZSBDYXRhbHVueWEiLCJpYXQiOjE1NDE2OTIwNTMsImV4cCI6MTU0MTY5MjM1Mywic3ViIjoiMWFkNGIzM2EyNTc5YTJhOThlZDciLCJub25jZSI6IjFmNTE3MmJiMjVhMjVlZjE3NmU1IiwiaHR0cHM6Ly9wdXJsLmltc2dsb2JhbC5vcmcvc3BlYy9sdGkvY2xhaW0vdmVyc2lvbiI6IjEuMy4wIiwibG9jYWxlIjoiZW4tVVMiLCJodHRwczovL3B1cmwuaW1zZ2xvYmFsLm9yZy9zcGVjL2x0aS9jbGFpbS9sYXVuY2hfcHJlc2VudGF0aW9uIjp7ImRvY3VtZW50X3RhcmdldCI6ImlmcmFtZSIsImhlaWdodCI6MzIwLCJ3aWR0aCI6MjQwLCJyZXR1cm5fdXJsIjoiaHR0cHM6Ly9sdGktcmkuaW1zZ2xvYmFsLm9yZy9wbGF0Zm9ybXMvNjgvcmV0dXJucyJ9LCJodHRwczovL3d3dy5leGFtcGxlLmNvbS9leHRlbnNpb24iOnsiY29sb3IiOiJ2aW9sZXQifSwiaHR0cHM6Ly9wdXJsLmltc2dsb2JhbC5vcmcvc3BlYy9sdGkvY2xhaW0vY3VzdG9tIjp7Im15Q3VzdG9tVmFsdWUiOiIxMjMifSwiaHR0cHM6Ly9wdXJsLmltc2dsb2JhbC5vcmcvc3BlYy9sdGkvY2xhaW0vZGVwbG95bWVudF9pZCI6IiJ9.S3If5Gz83DW-ZvSqqRnq-ga8uYj-kcOQ2JxuJtqjjERa3x6Z0KB_Sg9LIzgDCg2NqmuLSjP0VT6igbAUV2MekB1nGvjtMkJFxVczBe6BdqlZcDuoWIrxRlRUZ-03KPfLN7ErT4tJc-UksOgeV5m_3o3vIeoaAaJA0Vp8CfQgof54UNMmNEL44a-uOnFOSDptqkRuRRlHm-5UJ2vf2ZzFIyyOil0zJ_SOihpLTf56hAw9DpFWlN2uq0RlKXIPa1HCUrpHgstuez9r7shx1OU_gO9nNHu97x06PamCsPXo5h5wKmaC_MQZqnOosOzdvHq-Ft2hwFEIKIfswivgVmQfOw";
		sut.decode(token);
	}

	@Test(expected = InvalidTokenException.class)
	public void decodeFailWithInvalidToken() {
		String token = "invalid token";

		sut.decode(token);
		Assert.fail();
	}

	@Test
	public void validate() {
		String token = "eyJhbGciOiJSUzI1NiIsImtpZCI6InBVYUFkb2VmQ2Q1VGctVEM4MDdtalJlSGpTM2VjOG5zWTktbnJwV0RRUzAifQ.eyJodHRwczovL3B1cmwuaW1zZ2xvYmFsLm9yZy9zcGVjL2x0aS9jbGFpbS9tZXNzYWdlX3R5cGUiOiJMdGlSZXNvdXJjZUxpbmtSZXF1ZXN0IiwiZ2l2ZW5fbmFtZSI6IkVsZG9uIiwiZmFtaWx5X25hbWUiOiJIb3BwZSIsIm1pZGRsZV9uYW1lIjoiUm9kbmV5IiwicGljdHVyZSI6Imh0dHA6Ly9leGFtcGxlLm9yZy9FbGRvbi5qcGciLCJlbWFpbCI6IkVsZG9uLkhvcHBlQGV4YW1wbGUub3JnIiwibmFtZSI6IkVsZG9uIFJvZG5leSBIb3BwZSIsImh0dHBzOi8vcHVybC5pbXNnbG9iYWwub3JnL3NwZWMvbHRpL2NsYWltL3JvbGVzIjpbImh0dHA6Ly9wdXJsLmltc2dsb2JhbC5vcmcvdm9jYWIvbGlzL3YyL21lbWJlcnNoaXAjTGVhcm5lciIsImh0dHA6Ly9wdXJsLmltc2dsb2JhbC5vcmcvdm9jYWIvbGlzL3YyL2luc3RpdHV0aW9uL3BlcnNvbiNTdHVkZW50IiwiaHR0cDovL3B1cmwuaW1zZ2xvYmFsLm9yZy92b2NhYi9saXMvdjIvbWVtYmVyc2hpcCNNZW50b3IiXSwiaHR0cHM6Ly9wdXJsLmltc2dsb2JhbC5vcmcvc3BlYy9sdGkvY2xhaW0vcm9sZV9zY29wZV9tZW50b3IiOlsiYTYyYzUyYzAyYmEyNjIwMDNmNWUiXSwiaHR0cHM6Ly9wdXJsLmltc2dsb2JhbC5vcmcvc3BlYy9sdGkvY2xhaW0vcmVzb3VyY2VfbGluayI6eyJpZCI6IjMxOSIsInRpdGxlIjoiVGVzdCBUb29sIiwiZGVzY3JpcHRpb24iOiJMYXVuY2ggdGVzdCB0b29sIn0sImh0dHBzOi8vcHVybC5pbXNnbG9iYWwub3JnL3NwZWMvbHRpL2NsYWltL2NvbnRleHQiOnsiaWQiOiI4OCIsImxhYmVsIjoiVGVzdCBDb3Vyc2UiLCJ0aXRsZSI6IlRlc3QgQ291cnNlIiwidHlwZSI6WyJDb3Vyc2VPZmZlcmluZyJdfSwiaHR0cHM6Ly9wdXJsLmltc2dsb2JhbC5vcmcvc3BlYy9sdGkvY2xhaW0vdG9vbF9wbGF0Zm9ybSI6eyJuYW1lIjoiVU9DIFRlc3QgcGxhdGZvcm0iLCJjb250YWN0X2VtYWlsIjoiIiwiZGVzY3JpcHRpb24iOiIiLCJ1cmwiOiIiLCJwcm9kdWN0X2ZhbWlseV9jb2RlIjoiIiwidmVyc2lvbiI6IjEuMCJ9LCJodHRwczovL3B1cmwuaW1zZ2xvYmFsLm9yZy9zcGVjL2x0aS1hZ3MvY2xhaW0vZW5kcG9pbnQiOnsic2NvcGUiOlsiaHR0cHM6Ly9wdXJsLmltc2dsb2JhbC5vcmcvc3BlYy9sdGktYWdzL3Njb3BlL2xpbmVpdGVtIiwiaHR0cHM6Ly9wdXJsLmltc2dsb2JhbC5vcmcvc3BlYy9sdGktYWdzL3Njb3BlL3Jlc3VsdC5yZWFkb25seSIsImh0dHBzOi8vcHVybC5pbXNnbG9iYWwub3JnL3NwZWMvbHRpLWFncy9zY29wZS9zY29yZSJdLCJsaW5laXRlbXMiOiJodHRwczovL2x0aS1yaS5pbXNnbG9iYWwub3JnL3BsYXRmb3Jtcy82OC9jb250ZXh0cy84OC9saW5lX2l0ZW1zIn0sImh0dHBzOi8vcHVybC5pbXNnbG9iYWwub3JnL3NwZWMvbHRpLW5ycHMvY2xhaW0vbmFtZXNyb2xlc2VydmljZSI6eyJjb250ZXh0X21lbWJlcnNoaXBzX3VybCI6Imh0dHBzOi8vbHRpLXJpLmltc2dsb2JhbC5vcmcvcGxhdGZvcm1zLzY4L2NvbnRleHRzLzg4L21lbWJlcnNoaXBzLmpzb24iLCJzZXJ2aWNlX3ZlcnNpb24iOiIyLjAifSwiaHR0cHM6Ly9wdXJsLmltc2dsb2JhbC5vcmcvc3BlYy9sdGktY2VzL2NsYWltL2NhbGlwZXItZW5kcG9pbnQtc2VydmljZSI6eyJzY29wZXMiOlsiaHR0cHM6Ly9wdXJsLmltc2dsb2JhbC5vcmcvc3BlYy9sdGktY2VzL3YxcDAvc2NvcGUvc2VuZCJdLCJjYWxpcGVyX2VuZHBvaW50X3VybCI6Imh0dHBzOi8vbHRpLXJpLmltc2dsb2JhbC5vcmcvcGxhdGZvcm1zLzY4L3NlbnNvcnMiLCJjYWxpcGVyX2ZlZGVyYXRlZF9zZXNzaW9uX2lkIjoidXJuOnV1aWQ6OTgyMDJhYzdmYTAxOWVmMDAwNWQifSwiaXNzIjoiaHR0cHM6Ly93d3cudW9jLmVkdSIsImF1ZCI6IlVuaXZlcnNpdGF0IE9iZXJ0YSBkZSBDYXRhbHVueWEiLCJpYXQiOjE1NDE2OTIwNTMsImV4cCI6MTU0MTY5MjM1Mywic3ViIjoiMWFkNGIzM2EyNTc5YTJhOThlZDciLCJub25jZSI6IjFmNTE3MmJiMjVhMjVlZjE3NmU1IiwiaHR0cHM6Ly9wdXJsLmltc2dsb2JhbC5vcmcvc3BlYy9sdGkvY2xhaW0vdmVyc2lvbiI6IjEuMy4wIiwibG9jYWxlIjoiZW4tVVMiLCJodHRwczovL3B1cmwuaW1zZ2xvYmFsLm9yZy9zcGVjL2x0aS9jbGFpbS9sYXVuY2hfcHJlc2VudGF0aW9uIjp7ImRvY3VtZW50X3RhcmdldCI6ImlmcmFtZSIsImhlaWdodCI6MzIwLCJ3aWR0aCI6MjQwLCJyZXR1cm5fdXJsIjoiaHR0cHM6Ly9sdGktcmkuaW1zZ2xvYmFsLm9yZy9wbGF0Zm9ybXMvNjgvcmV0dXJucyJ9LCJodHRwczovL3d3dy5leGFtcGxlLmNvbS9leHRlbnNpb24iOnsiY29sb3IiOiJ2aW9sZXQifSwiaHR0cHM6Ly9wdXJsLmltc2dsb2JhbC5vcmcvc3BlYy9sdGkvY2xhaW0vY3VzdG9tIjp7Im15Q3VzdG9tVmFsdWUiOiIxMjMifSwiaHR0cHM6Ly9wdXJsLmltc2dsb2JhbC5vcmcvc3BlYy9sdGkvY2xhaW0vZGVwbG95bWVudF9pZCI6IiJ9.S3If5Gz83DW-ZvSqqRnq-ga8uYj-kcOQ2JxuJtqjjERa3x6Z0KB_Sg9LIzgDCg2NqmuLSjP0VT6igbAUV2MekB1nGvjtMkJFxVczBe6BdqlZcDuoWIrxRlRUZ-03KPfLN7ErT4tJc-UksOgeV5m_3o3vIeoaAaJA0Vp8CfQgof54UNMmNEL44a-uOnFOSDptqkRuRRlHm-5UJ2vf2ZzFIyyOil0zJ_SOihpLTf56hAw9DpFWlN2uq0RlKXIPa1HCUrpHgstuez9r7shx1OU_gO9nNHu97x06PamCsPXo5h5wKmaC_MQZqnOosOzdvHq-Ft2hwFEIKIfswivgVmQfOw";
		boolean result = sut.validate(token, false);
		Assert.assertTrue(result);
		Assert.assertNull(sut.getReason());
	}

	@Test
	public void validateWithInvalidTokenMustReturnFalse() {
		String token = "invalid token";
		boolean result = sut.validate(token);
		Assert.assertFalse(result);
		Assert.assertNotNull(sut.getReason());
	}

}
