<%@page import="java.util.Enumeration"%><%
	try
	{
		  
		String number1 = request.getParameter("number1");
		String number2 = request.getParameter("number2");
	
		if (number1 == null || number2 == null) {
			out.println("Error parameters number1 and number2 is required [" + number1 +","+ number2+ "]" );
			return;
		}
	
	
		Integer num1 = Integer.parseInt(number1);
		Integer num2 = Integer.parseInt(number2);
		out.print(num1 + num2);
		
	}
	catch(Exception e)
	{
		out.println("Error parameters number1 and number2 must be a number");
		e.printStackTrace();
		return;
	}
%>