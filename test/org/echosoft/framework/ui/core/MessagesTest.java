package org.echosoft.framework.ui.core;

import java.util.Iterator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Anton Sharapov
 */
public class MessagesTest {

    private Messages messages;

    @Before
    public void setup() {
        messages = new Messages();
        Assert.assertNull(messages.getMaximalSeverity());
        messages.addMessage( new Message("", Message.Severity.ERROR, "error1") );
        messages.addMessage( new Message("", Message.Severity.ERROR, "error1") );  // проверим как оно давит дубликаты ...
        messages.addMessage( new Message("", Message.Severity.ERROR, "error2") );
        messages.addMessage( new Message("", Message.Severity.WARN, "warn1") );
        Assert.assertEquals(Message.Severity.ERROR, messages.getMaximalSeverity());
        messages.addMessage( new Message("data", Message.Severity.WARN, "data warn1") );
        messages.addMessage( new Message("data", Message.Severity.INFO, "data info1") );
        messages.addMessage( new Message("data.navigator", Message.Severity.INFO, "navigator's info") );
        messages.addMessage( new Message("data.info", Message.Severity.FATAL, "some sheet", "detail info", null) );
        Assert.assertEquals(Message.Severity.FATAL, messages.getMaximalSeverity());
        Assert.assertEquals(7, messages.size());
    }

    @Test
    public void testAllMessages() {
        int cnt = 0;
        for (Iterator<Message> it = messages.messages(); it.hasNext(); ) {
            final Message msg = it.next();
            Assert.assertNotNull(msg);
            cnt++;
        }
        Assert.assertEquals(messages.size(), cnt);

        cnt = 0;
        for (Iterator<Message> it = messages.messages(null, null); it.hasNext(); ) {
            final Message msg = it.next();
            Assert.assertNotNull(msg);
            cnt++;
        }
        Assert.assertEquals(messages.size(), cnt);
    }

    @Test
    public void testClientMessages() {
        int cnt = 0;
        for (Iterator<Message> it = messages.messages("data"); it.hasNext(); ) {
            final Message msg = it.next();
            Assert.assertNotNull(msg);
            Assert.assertEquals("data", msg.getClientId());
            cnt++;
        }
        Assert.assertEquals(2, cnt);
    }

    @Test
    public void testSeverityMessages() {
        int cnt = 0;
        for (Iterator<Message> it = messages.messages(Message.Severity.WARN); it.hasNext(); ) {
            final Message msg = it.next();
            System.out.println(msg);
            Assert.assertNotNull(msg);
            Assert.assertTrue(msg.getSeverity().ordinal() <= Message.Severity.WARN.ordinal());
            cnt++;
        }
        Assert.assertEquals(5, cnt);
    }

    @Test
    public void testClientSeverityMessages() {
        int cnt = 0;
        for (Iterator<Message> it = messages.messages("", Message.Severity.ERROR); it.hasNext(); ) {
            final Message msg = it.next();
            Assert.assertNotNull(msg);
            Assert.assertEquals("", msg.getClientId());
            Assert.assertEquals(Message.Severity.ERROR, msg.getSeverity());
            cnt++;
        }
        Assert.assertEquals(2, cnt);
    }

    @Test
    public void testSavePoint() {
        Messages.SavePoint sp1 = messages.makeSavePoint();
        sp1.rollback();
        Assert.assertTrue(sp1.isAnnuled());
    }
}
