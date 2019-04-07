package com.amazonaws.lambda.demo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;
import java.sql.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.slu.Slot;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletV2;
import com.amazon.speech.speechlet.SessionEndedRequest;
import com.amazon.speech.speechlet.SessionStartedRequest;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SimpleCard;
public class InterviewSpeechlet  implements SpeechletV2{
	private static final Logger log = LoggerFactory.getLogger(InterviewSpeechlet.class);
 //public static Connection con;
 public static int counter=0;
 public static String convo="";
 public static String rollno;
	@Override
	public void onSessionStarted(SpeechletRequestEnvelope<SessionStartedRequest> requestEnvelope) {
		log.info("onSessionStarted requestId={}, sessionId={}", requestEnvelope.getRequest().getRequestId(),
				requestEnvelope.getSession().getSessionId());
		// any initialization logic goes here
	}

	@Override
	public SpeechletResponse onLaunch(SpeechletRequestEnvelope<LaunchRequest> requestEnvelope) {
		log.info("onLaunch requestId={}, sessionId={}", requestEnvelope.getRequest().getRequestId(),
				requestEnvelope.getSession().getSessionId());
	
		int random=(int)(Math.random()*100);
		String SpeechOutput;
		random=3;
		if(random%2==0)
		{
		 SpeechOutput=" Welcome to Alexa Interview Skill. Sorry, No interview scheduled now. Thank You.";
		}
		else
		{
		SpeechOutput="Welcome to Alexa Interview Skill. You can say your netra id like, my netra id is sixteen zero five one thirty eight. ";	
		}
		 
		 String reprompt="Dont feel hesitant to ask your query";
		 return newAskResponse(SpeechOutput,reprompt);
		 
		}
         
	@Override
	public SpeechletResponse onIntent(SpeechletRequestEnvelope<IntentRequest> requestEnvelope) {
		IntentRequest request = requestEnvelope.getRequest();
		log.info("onIntent requestId={}, sessionId={}", request.getRequestId(),
				requestEnvelope.getSession().getSessionId());

		Intent intent = request.getIntent();
		String intentName = (intent != null) ? intent.getName() : null;
		Session session = requestEnvelope.getSession();
		if("AuthenticateIntent".equals(intentName))
		{
			return startInterview(intent,session);
		}
		else if("RepeatQuestionIntent".equals(intentName))
		{
			return repeat(intent,session);
		}
		else if("YESIntent".equals(intentName))
		{
			return yesMethod(intent,session);
		}
		else if("AnswerIntent".equals(intentName))
			return answerMethod(intent,session);
		else if("SkipIntent".equals(intentName))
			return skipMethod(intent,session);
		/*else if("EndIntent".equals(intentName))
			return EndMethod(intent,session);*/
		else if ("AMAZON.HelpIntent".equals(intentName)) {
			return getHelp();
		} else if ("AMAZON.StopIntent".equals(intentName)) {
			PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
			outputSpeech.setText("Goodbye");

			return SpeechletResponse.newTellResponse(outputSpeech);
		} else if ("AMAZON.CancelIntent".equals(intentName)) {
			PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
			outputSpeech.setText("Goodbye");

			return SpeechletResponse.newTellResponse(outputSpeech);
		} else {
			String errorSpeech = "This is unsupported.  Please try something else.";
			return newAskResponse(errorSpeech, errorSpeech);
		}
		}
	private SpeechletResponse getHelp() {
		String speechOutput = "you can say next question for the next question ,to repeat the question say repeat,to quit the interview say end interview";
		String repromptText = "you can say next question for the next question ,to repeat the question say repeat,to quit the interview say end interview";
		return newAskResponse(speechOutput, repromptText);
	}
	private SpeechletResponse skipMethod(Intent intent,final Session session)
	{
		int totalquestions = (int) session.getAttribute("totalquestions");int qcount=(int)session.getAttribute("qcount");
		String resString = null;
		// check all the questions are completed or not
		if (qcount < totalquestions) {
			try {
				resString = getQuestion(session);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			resString="You are done with your interview,Goodbye";
		}
		String responseText = resString;

		return newAskResponse(responseText, responseText);
	}
	private SpeechletResponse yesMethod(Intent intent,final Session session)
	{
		Slot Yes=intent.getSlot("type");
        String user_ans=Yes.getValue();
        String resString="";
        int c=0; 
        int d=0;
        if(user_ans.equalsIgnoreCase("yes"))
               {
        	int random=(int)(Math.random()*100);
        	random=3;
        	if(random%2==0)
        	{
        		resString="you are not enrolled for this interview and Good Bye.";
        		}
        	else
        	{
        		if(c!=0)
        		{
        			resString="you already finished your interview,Goodbye";
        			
        		}
        		else
        		{
        			if(d!=0)
        			{
        				resString="No interview scheduled now for this ID";
        				
        			}
        			else
        			{
        				resString=getQuestion(session);
        			}
        		}
        	}
        }
        else
        {
        	resString="tell your netra id";
        }
        String responseText=resString;
        return newAskResponse(responseText, responseText);
	}
	private SpeechletResponse answerMethod(Intent intent,final Session session) {
		Slot answer_is=intent.getSlot("answer");
		int i=(int)session.getAttribute("qcount");
	        String answer=answer_is.getValue();	
	        session.setAttribute("ans"+i,answer);
	        String resString = null;String temp;
			try {

temp = getQuestion(session);

resString = temp;
}
catch (Exception e) {
		System.out.println(e.getMessage());
	}

	String responseText = resString;
	return newAskResponse(responseText, responseText);
	}
	 /*private SpeechletResponse EndMethod(Intent intent,final Session session)
	 {
		 try {
			 System.out.println("statement 1");
				Class.forName("com.mysql.jdbc.Driver"); //com.mysql.jdbc.Driver //org.gjt.mm.mysql.Driver
				 System.out.println("statement 2");
				Connection con=DriverManager.getConnection("jdbc:mysql://interview.c7jqpq8melms.us-east-1.rds.amazonaws.com:3306/interview","haripriya","haripriya");
				 System.out.println("statement 3");
				 
				String query="select * from student where Roll_No = '"+rollno+"'";
				Statement st=con.createStatement();
				 System.out.println("statement 4");
				ResultSet rs;
				rs=st.executeQuery(query);
				rs.next();
				 System.out.println("statement 5");
				 ResultSetMetaData rsmd = rs.getMetaData();
				 int columnCount = rsmd.getColumnCount();

				 // The column count starts from 1
				 for (int i = 1; i <= columnCount; i++ ) {
				   String name = rsmd.getColumnName(i);
				   // Do stuff with name
				   System.out.println(name);
				 }
			
				 int interview_no=(int)rs.getInt("interview_count");
				 System.out.println("interview_no");
				interview_no+=1;
				 System.out.println("interview_no");
				 String query1 = "update student set interview_count = ? where Roll_No = ?";
			      PreparedStatement preparedStmt = con.prepareStatement(query1);
			      System.out.println("statement 6");
			      preparedStmt.setInt   (1,interview_no);
			      preparedStmt.setString(2,rollno);
			      System.out.println("statement 7");
			      // execute the java preparedstatement
			      preparedStmt.executeUpdate();
			      System.out.println("statement 8");
			     rs=st.executeQuery(query);rs.next();
			      System.out.println("statement 9");
			      System.out.println(rs.getString("Username")+" "+rs.getInt("interview_count"));;
			      con.close();
			      System.out.println(convo);
			}
			 catch (Exception e)
		    {
		      System.out.println("Got an exception! ");
		      System.out.println(e.getMessage());
		    }
		 String responseText="Goodbye";
		 return newAskResponse(responseText,responseText);
	 }*/
	private SpeechletResponse repeat(Intent intent,final Session session)
	{

 System.out.println("in repeat method");
 counter=1;
 //session.getAttribute("qcount");
		String resString = null;String temp;
					try {
 temp = getQuestion(session);
 counter=0;
	resString = temp;
}
		catch (Exception e) {
				System.out.println(e.getMessage());
			}

			String responseText = resString;
			return newAskResponse(responseText, responseText);
	}
	public SpeechletResponse startInterview(Intent intent,final Session session)
	{
	     Slot netraid=intent.getSlot("netraid");
	     String netra=netraid.getValue();
	     session.setAttribute("NetraId",netra);
	     session.setAttribute("qcount",0);
	    convo+="uname"+"*===*";
	     String resString;
	     resString="Your netra id is "+netra+" to confirm say yes otherwise say no";
	 	String responseText = resString;

		return newAskResponse(responseText, responseText);
		
	}
	private String getQuestion(final Session session) {

		String responseText = "";
		System.out.println("====== In getQuestion start=====");
		String jsonTxt = "";
		try {
			Path path = Paths.get(InterviewSpeechlet.class.getResource("/").toURI());

			// The path for json file in Lambda Instance -
			String resourceLoc = path + "/resources/" + "current" + ".json";

			File f = new File(resourceLoc);
			if (f.exists()) {

				InputStream is;
				String responseString = "";
				try {
					is = new FileInputStream(f);

					BufferedReader streamReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
					String inputLine;
					StringBuffer response = new StringBuffer();
					while ((inputLine = streamReader.readLine()) != null) {
						response.append(inputLine);
					}
					responseString = response.toString();
				} catch (Exception e) {
					System.out.println("file does not exists");
					System.out.println(resourceLoc);
					responseText = "no questions available in this topic";
				}
				int ques=(int)session.getAttribute("qcount");
				if(ques<5) {
					
				
				// print in String
				// System.out.println(" ---getJsonData--- " + responseString);
				jsonTxt = responseString;// returns JSONArray as a string
				// converting string to JSONArray
				JSONArray allQuestions = new JSONArray(jsonTxt);
				session.setAttribute("totalquestions",5);
				try {
					JSONObject question;
					//JSONArray alloptions;
					if(counter==1)
					{
						int index=(int)session.getAttribute("currindex");
						question = (JSONObject) allQuestions.get(index - 1);
						// alloptions = (JSONArray) question.get("options");
					}else {
						ques+=1;
						session.setAttribute("qcount",ques);
					 int randno=(int)(Math.random()*100);
					 while(randno>35)
					 {
						 randno=(int)(Math.random()*100);
					 }
					 session.setAttribute("currindex", randno);
					 question = (JSONObject) allQuestions.get(randno - 1);
					// alloptions = (JSONArray) question.get("options");
					 }
					
					
					responseText = question.getString("question");
					} catch (Exception e) { 
					return responseText;
				}
			}
				else
				{
					responseText="thank you for taking the interview,GoodBye";
				//endOfInterview(session);
							
				}
				} else {
				System.out.println("file does not exists");
				System.out.println(resourceLoc);
				responseText = "no questions available in this topic";
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			responseText = "no questions available in this topic";
		}
		System.out.println(responseText);
		System.out.println("====== In getQuestion end =====");
		convo+=responseText+"*===*";
		return responseText;
	}


	@Override
	public void onSessionEnded(SpeechletRequestEnvelope<SessionEndedRequest> requestEnvelope) {
		log.info("onSessionEnded requestId={}, sessionId={}", requestEnvelope.getRequest().getRequestId(),
				requestEnvelope.getSession().getSessionId());
		
		// any cleanup logic goes here
	}
	
	private SpeechletResponse newAskResponse(String stringOutput, String repromptText) {
		PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
		outputSpeech.setText(stringOutput);

		PlainTextOutputSpeech repromptOutputSpeech = new PlainTextOutputSpeech();
		repromptOutputSpeech.setText(repromptText);
		Reprompt reprompt = new Reprompt();
		reprompt.setOutputSpeech(repromptOutputSpeech);

		return SpeechletResponse.newAskResponse(outputSpeech, reprompt);
	}

}
