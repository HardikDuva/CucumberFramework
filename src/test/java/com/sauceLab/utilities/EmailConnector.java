package com.sauceLab.utilities;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.mail.Store;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.MessagingException;
import javax.mail.BodyPart;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.SubjectTerm;
import java.io.IOException;
import java.util.Properties;

import static com.sauceLab.utilities.TestLogger.info;
import static com.sauceLab.utilities.TestLogger.error;
import static com.sauceLab.utilities.TestLogger.debug;

public final class EmailConnector {

    /**
     * There should be no instance of this class.
     */
    private EmailConnector() { }

    /**
     * The email of the email user.
     */
    private static final String USERNAME
            = TestConstants.EMAIL_USERNAME;

    /**
     * The password of the email user.
     */
    private static final String PASSWORD
            = TestConstants.EMAIL_PASSWORD;

    /**
     * The email store type
     */
    private static final String storeType
            = TestConstants.EMAIL_STORE_TYPE;

    /**
     * The email host
     */
    private static final String host
            = TestConstants.EMAIL_HOST;

    /**
     * The email port
     */
    private static final String port
            = TestConstants.EMAIL_PORT;

    /**
     * Determine if the Email was received by the user by checking their
     * inbox.
     * @param inputMailSubject The subject of the expected email.
     * @param myEmailAddress The email address of the recipient.
     * @return A {@link Boolean } set to true if the user received the email.
     */
    public static boolean ifEmailReceived(
            final String inputMailSubject,
            final String myEmailAddress) {
        final String folder = "INBOX";

        Store store;
        Folder inbox;
        Message[] messages;

        try {
            Properties properties
                    = getServerProperties(
                            true,
                            false);
            Session session = Session.getInstance(
                    properties, new Authenticator() {
                protected
                PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(USERNAME, PASSWORD);
                }
            });

            store = session.getStore(storeType);
            store.connect(host, USERNAME, PASSWORD);

            info("Fetching latest Inbox Data with email " + myEmailAddress);
            inbox = store.getFolder(folder);
            inbox.open(Folder.READ_ONLY);
            messages = inbox.search(new SubjectTerm(inputMailSubject));

            if (messages.length > 0) {
                for (Message message : messages) {
                    if (message.getSubject().contains(inputMailSubject)) {
                        if (message
                                .getRecipients(Message.RecipientType.TO)[0]
                                .toString().trim().
                                equalsIgnoreCase(myEmailAddress)) {
                            info("Mail Found");
                            return true;
                        }
                    }
                }
            } else {
                info("Messages was null");
                return false;
            }
        } catch (MessagingException e) {
            info("Mail not found with invalid email");
            return false;
        }

        return false;
    }

    /**
     * Retrieve the Email Data using the Subject and email address.
     * @param inputMailSubject The Email subject.
     * @param myEmailAddress The email address.
     * @return A {@link String } containing the email data.
     */
    public static String getEmailData(
            final String inputMailSubject,
            final String myEmailAddress) {

        final String folder = "INBOX";

        Store store;
        Folder inbox;
        String mailData = null;
        Message[] messages;

        try {
            Properties properties
                    = getServerProperties(
                    true,
                    false);
            Session session = Session.getInstance(
                    properties,
                    new Authenticator() {
                        protected
                        PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(USERNAME, PASSWORD);
                        }
                    });

            store = session.getStore(storeType);
            store.connect(host, USERNAME, PASSWORD);

            inbox = store.getFolder(folder);
            inbox.open(Folder.READ_ONLY);
            messages = inbox.search(new SubjectTerm(inputMailSubject));
            debug("Fetching latest Inbox Data for Subject ["
                    + inputMailSubject
                    + "] and To address is ["
                    + myEmailAddress
                    + "]");

            if (messages.length > 0) {
                for (Message message : messages) {
                    if (message.getSubject().contains(inputMailSubject)) {
                        if (message.getRecipients(
                                Message.RecipientType.TO)[0].toString().trim().
                                equalsIgnoreCase(myEmailAddress)) {
                            info("Mail Found, Now Searching mail data");

                            if (message.isMimeType("multipart/*")) {
                                info("multipart");
                                MimeMultipart mimeMultipart
                                        = (MimeMultipart) message.getContent();
                                mailData = getTextFromMimeMultipart(
                                        mimeMultipart);
                            } else if (message.isMimeType("text/html")) {
                                info("text/html");
                                mailData = (String) message.getContent();
                            } else {
                                info("The mail type was not recognized");
                            }
                            break;
                        }
                    }
                }
            } else {
                info("Messages was null");
            }
        } catch (MessagingException | IOException e) {
            error("Could not retrieve email with subject: ["
                    + inputMailSubject
                    + "] for user: ["
                    + myEmailAddress
                    + "]", e);
        }
        return mailData;
    }

    /**
     * Retrieve the Server Properties using the provided protocol and settings.
     * @param tls Turn Transport Layer Security on/off.
     * @param ssl Turn Secure Sockets Layer on/off.
     * @return A {@link Properties } object containing the server properties.
     */
    private static Properties getServerProperties(
            final boolean tls,
            final boolean ssl) {

        Properties properties = new Properties();

        // server setting
        properties.put(
                String.format(
                        "mail.%s.host",
                        storeType), host);
        properties.put(
                String.format(
                        "mail.%s.port",
                        storeType), port);

        properties.setProperty(
                String.format(
                        "mail.%s.auth",
                        storeType), "true");
        properties.setProperty(
                String.format(
                        "mail.%s.starttls.enable",
                        storeType), String.valueOf(tls));

        if (ssl) {
            // SSL setting
            properties.setProperty(
                    String.format(
                            "mail.%s.socketFactory.class",
                            storeType), "javax.net.ssl.SSLSocketFactory");
            properties.setProperty(
                    String.format(
                            "mail.%s.socketFactory.fallback",
                            storeType), "false");
            properties.setProperty(
                    String.format(
                            "mail.%s.socketFactory.port",
                            storeType), port);
        }

        return properties;
    }

    /**
     * Retrieve the text content from the Mime Multipart of the Email.
     * @param mimeMultipart The MimeMultipart of the Email.
     * @return A {@link String } containing the text of the email.
     */
    private static String getTextFromMimeMultipart(
            final MimeMultipart mimeMultipart) {
        StringBuilder result = new StringBuilder();
        try {
            int count = mimeMultipart.getCount();
            BodyPart bodyPart;
            if (count > 0) {
                bodyPart = mimeMultipart.getBodyPart(0);
                if (bodyPart.isMimeType("text/plain")) {
                    result.append("\n").append(bodyPart.getContent());
                } else {
                    for (int i = 1; i < count; i++) {
                        bodyPart = mimeMultipart.getBodyPart(i);
                        if (bodyPart.isMimeType("text/html")) {
                            result
                                    = new StringBuilder((String)
                                    bodyPart.getContent());
                        } else if (bodyPart.getContent()
                                instanceof MimeMultipart) {
                            result.append(getTextFromMimeMultipart(
                                    (MimeMultipart) bodyPart.getContent()));
                        }
                    }
                }
                return String.valueOf(result);
            }

        } catch (MessagingException | IOException e) {
            error("An Exception was caught while retrieving "
                    + "text from mime multipart", e);
        }
        return result.toString();
    }

    /**
     * Retrieve the HyperLink from an Email as a String.
     * @param htmlMailContent The email content.
     * @param hyperLinkText The expected hyperlink text.
     * @return A {@link String } containing the hyperlink from the email.
     */
    public static String getHyperLinkUsingLinkTextFromEmailHTMLContent(
            final String htmlMailContent,
            final String hyperLinkText) {
        Document document = Jsoup.parse(htmlMailContent);
        Elements links = document.select("a[href]");
        for (Element link : links) {
            info("Link found  is [" + link + "]");
            if (link.text().trim().equalsIgnoreCase(hyperLinkText)) {
                return link.attr("href");
            }
        }
        return null;
    }

}
