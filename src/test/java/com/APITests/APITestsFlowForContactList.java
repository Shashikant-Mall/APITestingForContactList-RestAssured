package com.APITests;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import org.testng.Assert;
import org.testng.annotations.*;

import com.POJOFiles.Contact;
import com.POJOFiles.User;
import io.restassured.response.Response;

public class APITestsFlowForContactList {
	
	 //Create object of User POJO class
	 User data = new User();
	 
	 //Create Object of Contact class
	 Contact contactData = new Contact();
	 
	 String token;
	 String emailId;
	 String id;
	
  @Test(priority=1)
  public void testTC01_AddNewUser() {
	  
	  //Create Payload  
	  data.setFirstName("Test");
	  data.setLastName("User");
	  data.setEmail("test" + System.currentTimeMillis() + "@1fake.com");
	  data.setPassword("myPassword");
	  
	  Response res = given()
	  .header("Content-Type","application/json")
	  .body(data)
	  
	  .when()
	  .post("https://thinking-tester-contact-list.herokuapp.com/users");
	  
	  //Validation
	  Assert.assertEquals(res.getStatusCode(), 201);
	  Assert.assertTrue(res.statusLine().contains("Created"));
	  
	  //Log the body
	  res.then().log().body();
	  
	 //Get the token
	  token=res.jsonPath().getString("token");
  }
  
  @Test(priority=2)
  public void testTC02_GetUserProfile() {
	  
	  given()
	  .header("Authorization",token)
	  
	  .when()
	  .get("https://thinking-tester-contact-list.herokuapp.com/users/me")
	  
	  .then()
	  .statusCode(200)
	  .statusLine(containsString("OK"));
  }
  
  @Test(priority=3)
  public void testTC03_UpdateUser() {
	  
	  //Create PayLoad
	  data.setFirstName("Updated");
	  data.setLastName("UserName");
	  data.setEmail("test" + System.currentTimeMillis() + "@fake.com");
	  data.setPassword("myNewPassword");
	  
	  Response res = given()
	  .header("Authorization",token)
	  .header("Content-Type","application/json")
	  .body(data)
	  
	  .when()
	  .patch("https://thinking-tester-contact-list.herokuapp.com/users/me");
	  
	  res.then()
	  .statusCode(200)
	  .statusLine(containsString("OK"))
	  .log().body();
	  
	  
	  //Get emailId
	  emailId=res.jsonPath().getString("email");
	  System.out.println("Updated emailId is :"+emailId);
  }
  
  @Test(priority=4,dependsOnMethods = "testTC03_UpdateUser")
  public void testTC04_LoginUser() {
	  
	  //Create Payload
      data.setEmail(emailId);
      data.setPassword("myNewPassword");
	  
	  given()
	  .header("Content-Type","application/json")
	  .body(data)
	  
	  .when()
	  .post("https://thinking-tester-contact-list.herokuapp.com/users/login")
	  
	  .then()
	  .statusCode(200)
	  .statusLine(containsString("OK"));
  }
  @Test(priority=5)
  public void testTC05_AddContact() {
	  
	  //Create Payload
	  contactData.setFirstName("Jhon");
	  contactData.setLastName("Doe");
	  contactData.setBirthdate("1970-01-01");
	  contactData.setEmail("jDoe"+System.currentTimeMillis()+"@fake.com");
	  contactData.setPhone("8976435217");
	  contactData.setStreet1("1 Main st");
	  contactData.setStreet2("Apartment A");
	  contactData.setCity("AnyTown");
	  contactData.setStateProvince("KS");
	  contactData.setPostalCode("12345");
	  contactData.setCountry("USA");
	  
	  Response res = given()
	  .header("Content-Type","application/json")
	  .header("Authorization",token)
	  .body(contactData)
	  
	  .when()
	  .post("https://thinking-tester-contact-list.herokuapp.com/contacts");
	  
	  res.then()
	  .log().body()
	  .statusCode(201)
	  .statusLine(containsString("Created"));
	  
	  //Set id as variable
	  id=res.jsonPath().getString("_id");
  }
  
  @Test(priority=6)
  public void testTC06_GetContactList() {
	  
	  given()
	  .header("Authorization",token)
	  
	  .when()
	  .get("https://thinking-tester-contact-list.herokuapp.com/contacts")
	  
	  .then()
	  .log().body()
	  .statusCode(200)
	  .statusLine(containsString("OK"));
  }
  
  @Test(priority=7)
  public void testTC07_GetContact() {
	  
	  given()
	  .header("Authorization",token)
	  
	  .when()
	  .get("https://thinking-tester-contact-list.herokuapp.com/contacts/"+id)
	  
	  .then()
	  .log().body()
	  .statusCode(200)
	  .statusLine(containsString("OK"));
  }
  
  @Test(priority=8)
  public void testTC08_FullUpdateContact() {
	  
	  //Create payload
	  contactData.setFirstName("Amy");
	  contactData.setLastName("Miller");
	  contactData.setBirthdate("1992-02-02");
	  contactData.setEmail("amiller"+System.currentTimeMillis()+"@fake.com");
	  contactData.setPhone("8005554242");
	  contactData.setStreet1("13 School st");
	  contactData.setStreet2("Apt 5");
	  contactData.setCity("Washington");
	  contactData.setStateProvince("QC");
	  contactData.setPostalCode("A1A1A1");
	  contactData.setCountry("Canada");
	  
	  Response res = given()
	  .header("Content-Type","application/json")
	  .header("Authorization",token)
	  .body(contactData)
	  
	  .when()
	  .put("https://thinking-tester-contact-list.herokuapp.com/contacts/"+id);
	  
	  res.then()
	  .log().body()
	  .statusCode(200)
	  .statusLine(containsString("OK"));
	  
	  //Get email 
	  String updatedEmail=res.jsonPath().getString("email");
	  Assert.assertEquals(updatedEmail, contactData.getEmail());
  }
  
  @Test(priority=9)
  public void testTC09_PartialUpdateContact() {
	  
	  //Create payload
	  contactData.setFirstName("Anna");
	  
	  given()
	  .header("Content-Type","application/json")
	  .header("Authorization",token)
	  .body(contactData)
	  
	  .when()
	  .patch("https://thinking-tester-contact-list.herokuapp.com/contacts/"+id)
	  
	  .then()
	  .log().body()
	  .statusCode(200)
	  .statusLine(containsString("OK"))
	  .body("firstName", equalTo(contactData.getFirstName()));
  }
  
  @Test(priority=10)
  public void testTC10_LogoutUser() {
	  
	  given()
	  .header("Authorization",token)
	  
	  .when()
	  .post("https://thinking-tester-contact-list.herokuapp.com/users/logout")
	  
	  .then()
	  .statusCode(200)
	  .statusLine(containsString("OK"));
  }
  
}
