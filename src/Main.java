import me.marnic.jdl.CombinedSpeedProgressDownloadHandler;
import me.marnic.jdl.Downloader;
import me.marnic.jdl.SizeUtil;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;

public class Main {
    public static void customSetDownloadHandler(Downloader downloader) {
        downloader.setDownloadHandler(new CombinedSpeedProgressDownloadHandler(downloader) {
            @Override
            public void onDownloadStart() {
                super.onDownloadStart();
                // define custom actions to do before download starts
                System.out.println("Download starting...");
            }
            @Override
            public void onDownloadSpeedProgress(int downloaded, int maxDownload, int percent, int bytesPerSec) {
                // define actions to do on each progress update
                // (by default updates once every 250ms as defined in CombinedSpeedProgressDownloadHandler's onDownloadStart())
                System.out.println(SizeUtil.toHumanReadableFromBytes(bytesPerSec) + "/s " + percent + "%");
            }
            @Override
            public void onDownloadFinish() {
                super.onDownloadFinish();
                // define custom actions to do after download finishes
                System.out.println("Download finished");
            }
        });
    }

    public static <FileNotFoundException> void main(String[] args) throws UnsupportedEncodingException, URISyntaxException {
        // example files that doesn't require cookies
        String url_1 = "https://edmullen.net/test/rc.jpg";
        String url_2 = "https://freetestdata.com/wp-content/uploads/2022/02/Free_Test_Data_7MB_MP4.mp4";
        String url_4 = "https://github.com/MrMarnic/JIconExtractReloaded/releases/download/v1.0/JIconExtractReloaded.jar";
        // example file that requires cookies for login purpose
        String url_3 = "https://github.com/settings/profile";
        String customUserAgentString = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/115.0.0.0 Safari/537.36";

        // downloader without cookies
        Downloader downloader_1 = new Downloader(true);
        // with cookies, without domain filter
        Downloader downloader_2 = new Downloader(true);
        downloader_2.setCookies(new File("C:\\demo\\cookies-github-com.txt"));
        // with cookies, with domain filter
        // set domain filter before setting cookies
        Downloader downloader_3 = new Downloader(true);
        downloader_3.setDomainFilter("github.com");
        downloader_3.setCookies(new File("C:\\demo\\cookies-github-com.txt"));
        // without cookies, no default handler
        Downloader downloader_4 = new Downloader(false);
        downloader_4.setCustomUserAgentString(customUserAgentString);
        customSetDownloadHandler(downloader_4);
        // without cookies, with custom user agent string
        Downloader downloader_5 = new Downloader(true);
        downloader_5.setCustomUserAgentString(customUserAgentString);

        // get file size
        System.out.println(SizeUtil.toHumanReadableFromBytes(downloader_4.getDownloadLength(url_4)));
        // when no file name is provided in the method parameters, file name is retrieved from url
        downloader_4.downloadFileToLocation(url_4, "C:\\demo\\test downloads\\");

        // must create a new download handler to download another file
        customSetDownloadHandler(downloader_4);
        System.out.println(SizeUtil.toHumanReadableFromBytes(downloader_4.getDownloadLength(url_2)));
        downloader_4.downloadFileToLocation(url_2, "C:\\demo\\test downloads\\");

        // downloader_3 uses default handler, which doesn't output download speed or progress
        downloader_3.downloadFileToLocation(url_3, "C:\\demo\\test downloads\\", "GitHub Settings page.html");
        downloader_1.downloadFileToLocation("https://www.whatismybrowser.com/detect/what-is-my-user-agent/", "C:\\demo\\test downloads\\", "UserAgentTest_1.html");

        downloader_5.downloadFileToLocation("https://www.whatismybrowser.com/detect/what-is-my-user-agent/", "C:\\demo\\test downloads\\", "UserAgentTest_2.html");

        System.out.println("done");
    }
}