package com.sauceLab.utilities;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;


public class DateTimeConnector {

	public static String getTimeStamp() {
		return (new SimpleDateFormat("yyyyMMdd_HHmmss")).format(new Date());
	}

	public static String getTimeStampWithLocaleEnglish() {
        return LocalDateTime.now().format(DateTimeFormatter
				.ofPattern("dd MMMM yyyy HH:mm", Locale.ENGLISH));
	}

}
