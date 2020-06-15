package com.abreinig.nasa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

public class DateUtility {
        private static final Logger LOGGER = LoggerFactory.getLogger(DateUtility.class);

        public static LocalDate formatDate(String userDate) throws DateTimeParseException {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(""
                                + "[MMMM dd, yyyy]"
                                + "[MMMM d, yyyy]"
                                + "[M/dd/yy]"
                                + "[MMM-dd-yyyy]"
                        , Locale.ENGLISH);

                try {
                        LocalDate formattedDate = LocalDate.parse(userDate, formatter);
                        LOGGER.info(String.valueOf(formattedDate));
                        return formattedDate;
                } catch (DateTimeParseException ex) {
                        LOGGER.error("DateTimeParseException:", ex);
                        return LocalDate.now();
                }
        }
}
