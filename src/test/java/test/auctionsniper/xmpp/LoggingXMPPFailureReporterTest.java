package test.auctionsniper.xmpp;

import org.junit.AfterClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.imposters.ByteBuddyClassImposteriser;
import org.jmock.integration.junit4.JMock;

import auctionsniper.xmpp.LoggingXMPPFailureReporter;

import java.util.logging.LogManager;
import java.util.logging.Logger;

@Ignore
@RunWith(JMock.class)
public class LoggingXMPPFailureReporterTest { 
  private final Mockery context = new Mockery() {{ 
    setImposteriser(ByteBuddyClassImposteriser.INSTANCE);
  }}; 
  final Logger logger = context.mock(Logger.class); 
  final LoggingXMPPFailureReporter reporter = new LoggingXMPPFailureReporter(logger);
  
  @AfterClass public static void resetLogging() { 
    LogManager.getLogManager().reset(); 
  } 
  @Test public void 
  writesMessageTranslationFailureToLog() { 
    context.checking(new Expectations() {{ 
      oneOf(logger).severe("<auction id> " 
                         + "Could not translate message \"bad message\" " 
                         + "because \"java.lang.Exception: an exception\""); 
    }}); 
    
    reporter.cannotTranslateMessage("auction id", "bad message", 
                                    new Exception("an exception")); 
  } 
} 
