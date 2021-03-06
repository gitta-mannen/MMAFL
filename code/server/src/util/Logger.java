package util;

import java.util.ArrayList;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import settings.Constants;
import settings.Constants.*;

public class Logger {
	private static ArrayList<Object[]> log;
    private static final int STACK_INDEX;

	static {
		log = new  ArrayList<Object[]>();
        // Finds out the index of "this code" in the returned stack trace - funny but it differs in JDK 1.5 and 1.6
        int i = 0;
        for (StackTraceElement ste: Thread.currentThread().getStackTrace())
        {
            i++;
            if (ste.getClassName().equals(Logger.class.getName()))
            {
                break;
            }
        }
        STACK_INDEX = i;
	}
	
	public static synchronized  void log (Object message, boolean printToConsole) {
		StackTraceElement st;
		if (message instanceof Exception) {
			st = ((Exception) message).getStackTrace()[0];
		} else {
			st = Thread.currentThread().getStackTrace()[STACK_INDEX + 0];
		}
		long threadId = Thread.currentThread().getId();
		String className = st.getClassName();
		String MethodName = st.getMethodName();
		int line = st.getLineNumber();
		String time;
		
		Calendar cal = Calendar.getInstance();
    	cal.getTime();
    	SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-DD HH:mm:ss");    	
    	time = sdf.format(cal.getTime());
    	
		log.add(new Object[]{time, threadId, className, MethodName, message});
		String logEntry = "[" + time + "]" + " [Thread: " + threadId + "][Class: " + className
				+ "][Method: " + MethodName +  "][Line: " + line + "]>>> " + message + "\r\n";
		
//		File logFile = new File(Constants.APP_ROOT.resolve("log.txt"));
//		
//		try {
//			util.WebDiskCache.stringTofile(logEntry, logFile, true);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		if (printToConsole) {
			System.out.println(logEntry);	
		}
		
	}
	/*
    public static String getCurrentMethodName(StackTraceElement[] st)
    {
        return getCurrentMethodName(1, st);     // making additional overloaded method call requires +1 offset
    }

    private static String getCurrentMethodName(int offset, StackTraceElement[] st)
    {
        return st[STACK_INDEX + offset].getMethodName();
    }

    public static String getCurrentClassName(StackTraceElement[] st)
    {
        return getCurrentClassName(1, st);      // making additional overloaded method call requires +1 offset
    }

    private static String getCurrentClassName(int offset, StackTraceElement[] st)
    {
    return st[STACK_INDEX + offset].getClassName();
    }

    public static String getCurrentFileName()
    {
        return getCurrentFileName(1);     // making additional overloaded method call requires +1 offset
    }

    private static String getCurrentFileName(int offset)
    {
        String filename = Thread.currentThread().getStackTrace()[STACK_INDEX + offset].getFileName();
        int lineNumber = Thread.currentThread().getStackTrace()[STACK_INDEX + offset].getLineNumber();

        return filename + ":" + lineNumber;
    }

    
    public static String getInvokingMethodName()
    {
        return getInvokingMethodName(2); 
    }

    private static String getInvokingMethodName(int offset)
    {
        return getCurrentMethodName(offset + 1);    // re-uses getCurrentMethodName() with desired index
    }

    public static String getInvokingClassName()
    {
        return getInvokingClassName(2); 
    }

    private static String getInvokingClassName(int offset)
    {
        return getCurrentClassName(offset + 1);     // re-uses getCurrentClassName() with desired index
    }

    public static String getInvokingFileName()
    {
        return getInvokingFileName(2); 
    }

    private static String getInvokingFileName(int offset)
    {
        return getCurrentFileName(offset + 1);     // re-uses getCurrentFileName() with desired index
    }

    public static String getCurrentMethodNameFqn()
    {
        return getCurrentMethodNameFqn(1);
    }

    private static String getCurrentMethodNameFqn(int offset)
    {
        String currentClassName = getCurrentClassName(offset + 1);
        String currentMethodName = getCurrentMethodName(offset + 1);

        return currentClassName + "." + currentMethodName ;
    }

    public static String getCurrentFileNameFqn()
    {
        String CurrentMethodNameFqn = getCurrentMethodNameFqn(1);
        String currentFileName = getCurrentFileName(1);

        return CurrentMethodNameFqn + "(" + currentFileName + ")";
    }

    public static String getInvokingMethodNameFqn()
    {
        return getInvokingMethodNameFqn(2);
    }

    private static String getInvokingMethodNameFqn(int offset)
    {
        String invokingClassName = getInvokingClassName(offset + 1);
        String invokingMethodName = getInvokingMethodName(offset + 1);

        return invokingClassName + "." + invokingMethodName;
    }

    public static String getInvokingFileNameFqn()
    {
        String invokingMethodNameFqn = getInvokingMethodNameFqn(2);
        String invokingFileName = getInvokingFileName(2);

        return invokingMethodNameFqn + "(" + invokingFileName + ")";
    }
    */
}
