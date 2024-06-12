package UnitTest;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import gui.UserScreens.LogInScreenController;
import javafx.event.ActionEvent;

public class LogInScreenControllerTest {
	   private LogInScreenController mocklogInScreenController;
	   private ActionEvent event;
	   

		@Before
		public void setUp() throws NoSuchMethodException, SecurityException {
			mocklogInScreenController = spy(new LogInScreenController());
		}

	  // Check Functionality: check login with null credentials
	  // Input Data: (null) getUsernameAndPassword()
	  // Expected Result: "FillAllUserCredentials"
	  @Test
	  public void TestloginNullCredentials() throws Exception {
		doReturn(null).when(mocklogInScreenController).getUsernameAndPassword();
		String expected = "FillAllUserCredentials";
		String result = mocklogInScreenController.logIn(event);
		assertEquals(expected, result);
	   }
	  
	  //Check functionality: check login with wrong credentials
	  //Input data: (ArrayList) credentials with wrong username and password
	  //Expected result: "WorngUserCredentials"
	  @Test
	  public void TestloginWorngCredentials() throws Exception {
		ArrayList<String> credentials = new ArrayList<>();
		credentials.add("WORNGusername");
		credentials.add("WORNGpassword");
		doReturn(credentials).when(mocklogInScreenController).getUsernameAndPassword();
		doReturn(false).when(mocklogInScreenController).requestLogIn(credentials);
		String expected = "WorngUserCredentials";
		String result = mocklogInScreenController.logIn(event);
		assertEquals(expected, result);
	   }
	  
	  //Check functionality: check if the user is not OL and not customer or subscriber
	  //Input data: username: NOTCustomerOrSubscriberusername, password: password
	  //Expected result: "Unauthorized account"
	  @Test
	  public void TestloginNOTOLNOTCustomerORSubscriber() throws Exception {
		ArrayList<String> credentials = new ArrayList<>();
		credentials.add("NOTCustomerOrSubscriberusername");
		credentials.add("password");
		doReturn(credentials).when(mocklogInScreenController).getUsernameAndPassword();
		doReturn(true).when(mocklogInScreenController).requestLogIn(credentials);
		doReturn(false).when(mocklogInScreenController).isOL();
		doReturn("NOT Customer OR Subscriber").when(mocklogInScreenController).getUserDepartment();
		doReturn(true).when(mocklogInScreenController).requestLogOut(credentials);
		String expected = "Unauthorized account";
		String result = mocklogInScreenController.logIn(event);
		assertEquals(expected, result);
	   }
	  
	  //Check functionality: check if the user is not OL and a customer
	  //Input data: username: Customerusername, password: password
	  //Expected result: "LoggedIn"
	  @Test
	  public void TestloginNOTOLCustomer() throws Exception {
		ArrayList<String> credentials = new ArrayList<>();
		credentials.add("Customerusername");
		credentials.add("password");
		doReturn(credentials).when(mocklogInScreenController).getUsernameAndPassword();
		doReturn(true).when(mocklogInScreenController).requestLogIn(credentials);
		doReturn(false).when(mocklogInScreenController).isOL();
		doReturn("customer").when(mocklogInScreenController).getUserDepartment();
		doReturn(true).when(mocklogInScreenController).loadUserHomeScreen(event);
		String expected = "LoggedIn";
		String result = mocklogInScreenController.logIn(event);
		assertEquals(expected, result);
	   }
	  
	  //Check functionality: check if the user is not OL and a subscriber
	  //Input data: username: Subscriberusername, password: password
	  //Expected result: "LoggedIn"
	  @Test
	  public void TestloginNOTOLSubscriber() throws Exception {
		ArrayList<String> credentials = new ArrayList<>();
		credentials.add("Subscriberusername");
		credentials.add("password");
		doReturn(credentials).when(mocklogInScreenController).getUsernameAndPassword();
		doReturn(true).when(mocklogInScreenController).requestLogIn(credentials);
		doReturn(false).when(mocklogInScreenController).isOL();
		doReturn("subscriber").when(mocklogInScreenController).getUserDepartment();
		doReturn(true).when(mocklogInScreenController).loadUserHomeScreen(event);
		String expected = "LoggedIn";
		String result = mocklogInScreenController.logIn(event);
		assertEquals(expected, result);
	   }
	  
	  //Check functionality: check successful OL login
	  //Input data: username: username, password: password
	  //Expected result: "LoggedIn"
	  @Test
	  public void TestloginOL() throws Exception {
		ArrayList<String> credentials = new ArrayList<>();
		credentials.add("username");
		credentials.add("password");
		doReturn(credentials).when(mocklogInScreenController).getUsernameAndPassword();
		doReturn(true).when(mocklogInScreenController).requestLogIn(credentials);
		doReturn(true).when(mocklogInScreenController).isOL();
		doReturn(true).when(mocklogInScreenController).loadUserHomeScreen(event);
		String expected = "LoggedIn";
		String result = mocklogInScreenController.logIn(event);
		assertEquals(expected, result);
	   }
}
