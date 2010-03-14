package org.echosoft.framework.ui.core;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Anton Sharapov
 */
public class AgentTest {

    private static Agent[] testcases = new Agent[] {
        new Agent(Agent.Type.GECKO, Agent.OS.LINUX, 1, 9, "Mozilla/5.0 (X11; U; Linux i686; pl-PL; rv:1.9.0.2) Gecko/20121223 Ubuntu/9.25 (jaunty) Firefox/3.8"),
        new Agent(Agent.Type.GECKO, Agent.OS.WINDOWS, 1, 9, "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US; rv:1.9.2b5) Gecko/20091204 Firefox/3.6b5"),
        new Agent(Agent.Type.GECKO, Agent.OS.LINUX, 1, 9, "Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.2.1) Gecko/20100122 firefox/3.6.1"),
        new Agent(Agent.Type.GECKO, Agent.OS.LINUX, 1, 9, "Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.2) Gecko/20100128 Gentoo Firefox/3.6"),
        new Agent(Agent.Type.GECKO, Agent.OS.WINDOWS, 1, 9, "Mozilla/5.0 (Windows; U; Windows NT 5.1; nl; rv:1.9.2) Gecko/20100115 Firefox/3.6 (.NET CLR 3.5.30729)"),
        new Agent(Agent.Type.GECKO, Agent.OS.LINUX, 1, 9, "Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.0.2) Gecko/2008092313 Ubuntu/8.04 (hardy) Firefox/3.1"),
        new Agent(Agent.Type.GECKO, Agent.OS.WINDOWS, 1, 9, "Mozilla/5.0 (Windows; U; Windows NT 6.1; nl; rv:1.9.0.9) Gecko/2009040821 Firefox/3.0.9 FirePHP/0.3"),
        new Agent(Agent.Type.GECKO, Agent.OS.LINUX, 1, 8, "Mozilla/5.0 (X11; U; Linux i686; nl-NL; rv:1.8.1.9) Gecko/20071105 Firefox/2.0.0.9"),
        new Agent(Agent.Type.GECKO, Agent.OS.WINDOWS, 1, 8, "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.8.1.9) Gecko/20071025 Firefox/2.0.0.9"),
        new Agent(Agent.Type.GECKO, Agent.OS.LINUX, 1, 8, "Mozilla/5.0 (X11; U; OpenBSD amd64; en-US; rv:1.8.0.9) Gecko/20070101 Firefox/1.5.0.9"),
        new Agent(Agent.Type.GECKO, Agent.OS.LINUX, 1, 7, "Mozilla/5.0 (X11; U; Linux i686; en-GB; rv:1.7.6) Gecko/20050405 Firefox/1.0 (Ubuntu package 1.0.2)"),
        new Agent(Agent.Type.GECKO, Agent.OS.WINDOWS, 1, 7, "Mozilla/5.0 (Windows; U; WinNT4.0; en-US; rv:1.7.5) Gecko/20041107 Firefox/1.0"),
        new Agent(Agent.Type.GECKO, Agent.OS.LINUX, 1, 9, "Mozilla/5.0 (X11; U; Gentoo Linux x86_64; pl-PL) Gecko Firefox"),

        new Agent(Agent.Type.IEXPLORER, Agent.OS.WINDOWS, 8, 0, "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.2; Trident/4.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0)"),
        new Agent(Agent.Type.IEXPLORER, Agent.OS.WINDOWS, 8, 0, "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1; WOW64; Trident/4.0; chromeframe; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; InfoPath.2; MS-RTC LM 8)"),
        new Agent(Agent.Type.IEXPLORER, Agent.OS.WINDOWS, 7, 0, "Mozilla/5.0 (Windows; U; MSIE 7.0; Windows NT 6.0; el-GR)"),
        new Agent(Agent.Type.IEXPLORER, Agent.OS.MACOS, 7, 0, "Mozilla/5.0 (MSIE 7.0; Macintosh; U; SunOS; X11; gu; SV1; InfoPath.2; .NET CLR 3.0.04506.30; .NET CLR 3.0.04506.648)"),
        new Agent(Agent.Type.IEXPLORER, Agent.OS.WINDOWS, 6, 1, "Mozilla/4.0 (compatible; MSIE 6.1; Windows XP; .NET CLR 1.1.4322; .NET CLR 2.0.50727)"),
        new Agent(Agent.Type.IEXPLORER, Agent.OS.WINDOWS, 6, 0, "Mozilla/4.0 (MSIE 6.0; Windows NT 5.1)"),
        new Agent(Agent.Type.IEXPLORER, Agent.OS.WINDOWS, 6, 0, "Mozilla/4.0 (compatible; MSIE 6.0; MSIE 5.5; Windows NT 5.1)"),
        new Agent(Agent.Type.IEXPLORER, Agent.OS.WINDOWS, 5, 5, "Mozilla/4.0 (compatible; MSIE 5.5; Windows NT)"),
        new Agent(Agent.Type.IEXPLORER, Agent.OS.WINDOWS, 5, 1, "Mozilla/4.0 (compatible; MSIE 5.01; Windows NT; .NET CLR 1.0.3705)"),
        new Agent(Agent.Type.IEXPLORER, Agent.OS.WINDOWS, 4, 1, "Mozilla/4.0 (compatible; MSIE 4.01; Windows CE)"),

        new Agent(Agent.Type.KONQUEROR, Agent.OS.LINUX, 4, 3, "Mozilla/5.0 (compatible; Konqueror/4.3; Linux) KHTML/4.3.1 (like Gecko) Fedora/4.3.1-3.fc11"),
        new Agent(Agent.Type.KONQUEROR, Agent.OS.LINUX, 4, 2, "Mozilla/5.0 (compatible; Konqueror/4.2; Linux) KHTML/4.2.96 (like Gecko)"),
        new Agent(Agent.Type.KONQUEROR, Agent.OS.LINUX, 4, 2, "Mozilla/5.0 (compatible; Konqueror/4.2) KHTML/4.2.4 (like Gecko) Fedora/4.2.4-2.fc11"),
        new Agent(Agent.Type.KONQUEROR, Agent.OS.LINUX, 4, 1, "Mozilla/5.0 (compatible; Konqueror/4.1; Linux 2.6.27.7-134.fc10.x86_64; X11; x86_64) KHTML/4.1.3 (like Gecko) Fedora/4.1.3-4.fc10"),
        new Agent(Agent.Type.KONQUEROR, Agent.OS.LINUX, 3, 5, "Mozilla/5.0 (compatible; Konqueror/3.5; Linux) KHTML/3.5.6 (like Gecko) (Kubuntu)"),
        new Agent(Agent.Type.KONQUEROR, Agent.OS.LINUX, 3, 1, "Mozilla/5.0 (compatible; Konqueror/3.1-rc6; i686 Linux; 20020915)"),
        new Agent(Agent.Type.KONQUEROR, Agent.OS.LINUX, 2, 2, "Mozilla/5.0 (compatible; Konqueror/2.2.2; Linux 2.4.14-xfs; X11; i686"),

        new Agent(Agent.Type.OPERA, Agent.OS.LINUX, 9, 70, "Opera/9.70 (Linux i686 ; U; ; en) Presto/2.2.1"),
        new Agent(Agent.Type.OPERA, Agent.OS.WINDOWS, 9, 7, "Opera 9.7 (Windows NT 5.2; U; en)"),
        new Agent(Agent.Type.OPERA, Agent.OS.WINDOWS, 9, 25, "Opera/9.25 (Windows NT 5.2; U; en)"),
        new Agent(Agent.Type.OPERA, Agent.OS.WINDOWS, 8, 65, "Mozilla/4.0 (compatible; MSIE 6.0; Windows CE; PPC; 240x240) Opera 8.65 [en]"),
        new Agent(Agent.Type.OPERA, Agent.OS.LINUX, 8, 51, "Opera/8.51 (X11; Linux i686; U; en)"),
        new Agent(Agent.Type.OPERA, Agent.OS.WINDOWS, 7, 11, "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1) Opera 7.11 [ru]"),
        new Agent(Agent.Type.OPERA, Agent.OS.SOLARIS, 5, 0, "Mozilla/5.0 (SunOS 5.8 sun4u; U) Opera 5.0 [en]"),
        new Agent(Agent.Type.OPERA, Agent.OS.LINUX, 9, 80, "Opera/9.80 (X11; Linux i686; U; Debian; pl) Presto/2.2.15 Version/10.00"),

        new Agent(Agent.Type.CHROME, Agent.OS.WINDOWS, 5, 0, "Mozilla/5.0 (Windows; U; Windows NT 5.2; en-US) AppleWebKit/532.9 (KHTML, like Gecko) Chrome/5.0.310.0 Safari/532.9"),
        new Agent(Agent.Type.CHROME, Agent.OS.LINUX, 5, 0, "Mozilla/5.0 (X11; U; Linux x86_64; en-US) AppleWebKit/532.9 (KHTML, like Gecko) Chrome/5.0.309.0 Safari/532.9"),
        new Agent(Agent.Type.CHROME, Agent.OS.MACOS, 4, 0, "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10_5_8; en-US) AppleWebKit/532.8 (KHTML, like Gecko) Chrome/4.0.302.2 Safari/532.8"),
        new Agent(Agent.Type.CHROME, Agent.OS.LINUX, 4, 0, "Mozilla/5.0 (X11; U; Linux i686; en-US) AppleWebKit/532.8 (KHTML, like Gecko) Chrome/4.0.277.0 Safari/532.8"),
        new Agent(Agent.Type.CHROME, Agent.OS.WINDOWS, 3, 0, "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US) AppleWebKit/532.0 (KHTML, like Gecko) Chrome/3.0.201.0 Safari/532.0"),
        new Agent(Agent.Type.CHROME, Agent.OS.WINDOWS, 2, 0, "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US) AppleWebKit/530.8 (KHTML, like Gecko) Chrome/2.0.178.0 Safari/530.8"),
        new Agent(Agent.Type.CHROME, Agent.OS.WINDOWS, 1, 0, "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US) AppleWebKit/525.19 (KHTML, like Gecko) Chrome/1.0.154.55 Safari/525.19"),

        new Agent(Agent.Type.LYNX, null, 2, 8, "Lynx/2.8.6rel.4 libwww-FM/2.14 SSL-MM/1.4.1 OpenSSL/0.9.8g"),
        new Agent(Agent.Type.LYNX, null, 2, 8, "Lynx/2.8.4rel.1"),
        new Agent(Agent.Type.LYNX, null, 2, 8, "Lynx/2.8.3dev.8 libwww-FM/2.14FM"),

        new Agent(Agent.Type.LINKS, Agent.OS.LINUX, 6, 9, "Links (6.9; Unix 6.9-astral sparc; 80x25)"),
        new Agent(Agent.Type.LINKS, Agent.OS.LINUX, 2, 0, "Links (2.xpre7; Linux 2.4.18 i586; x)"),
        new Agent(Agent.Type.LINKS, Agent.OS.LINUX, 2, 6, "Links (2.2; Linux 2.6.28-11-server i686; 80x24)"),
        new Agent(Agent.Type.LINKS, Agent.OS.MACOS, 2, 1, "Links (2.1pre33; Darwin 8.11.0 Power Macintosh; x)"),
/*
safari:
Mozilla/5.0 (Windows; U; Windows NT 6.1; ko-KR) AppleWebKit/531.21.8 (KHTML, like Gecko) Version/4.0.4 Safari/531.21.10
Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10_6_2; de-at) AppleWebKit/531.21.8 (KHTML, like Gecko) Version/4.0.4 Safari/531.21.10
Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US) AppleWebKit/530.19.2 (KHTML, like Gecko) Version/4.0.2 Safari/530.19.1
Mozilla/5.0 (Macintosh; U; PPC Mac OS X 10_5_7; en-us) AppleWebKit/530.19.2 (KHTML, like Gecko) Version/4.0.2 Safari/530.19
Mozilla/5.0 (Windows; U; Windows NT 5.1; nb-NO) AppleWebKit/525.28 (KHTML, like Gecko) Version/3.2.2 Safari/525.28.1
Mozilla/5.0 (Windows; U; Windows NT 5.2; ru-RU) AppleWebKit/525.13 (KHTML, like Gecko) Version/3.1 Safari/525.13.3
Mozilla/5.0 (Macintosh; U; PPC Mac OS X; en_CA) AppleWebKit/419 (KHTML, like Gecko) Safari/419.3
Mozilla/5.0 (Macintosh; U; PPC Mac OS X; en-us) AppleWebKit/416.12 (KHTML, like Gecko) Safari/416.13
Mozilla/5.0 (Macintosh; U; PPC Mac OS X; en-us) AppleWebKit/312.8.1 (KHTML, like Gecko) Safari/312.6
Mozilla/5.0 (Macintosh; U; PPC Mac OS X; en) AppleWebKit/312.8.1 (KHTML, like Gecko) Safari/312.6
seamonkey:
Seamonkey-1.1.13-1(X11; U; GNU Fedora fc 10) Gecko/20081112
Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.9.1.2pre) Gecko/20090723 SeaMonkey/2.0b2pre
Mozilla/5.0 (Windows; U; Windows NT 5.1; de; rv:1.9.1.7) Gecko/20100104 SeaMonkey/2.0.2
 */
    };

    @Test
    public void testAgents() {
        AssertionError err = null;
        for (int i=0; i<testcases.length; i++) {
            System.out.println(i+". "+testcases[i].getAgent());
            final Agent testcase = testcases[i];
            Agent agent = null;
            try {
                agent = Utils.detectUserAgent( testcase.getAgent() );
                Assert.assertNotNull(agent);
                Assert.assertEquals(agent.getAgent(), testcase.getType(), agent.getType());
                Assert.assertEquals(agent.getAgent(), testcase.getOperationSystem(), agent.getOperationSystem());
                Assert.assertEquals(agent.getAgent(), testcase.getMajorVersion(), agent.getMajorVersion());
                Assert.assertEquals(agent.getAgent(), testcase.getMinorVersion(), agent.getMinorVersion());
                Assert.assertEquals(agent.getAgent(), testcase, agent);
            } catch (AssertionError e) {
                if (err==null)
                    err = e;
                System.out.println("    [FAILED]  "+agent);
            }
        }
        System.out.println("-------------------------------------------------");
        if (err!=null)
            throw err;
    }

}
