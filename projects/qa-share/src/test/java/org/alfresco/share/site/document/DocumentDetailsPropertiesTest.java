/**
 * @author maryia.zaichanka
 */

package org.alfresco.share.site.document;

import java.util.Map;

import org.alfresco.po.share.site.document.DetailsPage;
import org.alfresco.po.share.site.document.DocumentLibraryPage;
import org.alfresco.po.share.site.document.EditDocumentPropertiesPage;
import org.alfresco.share.util.AbstractUtils;
import org.alfresco.share.util.ShareUser;
import org.alfresco.share.util.api.CreateUserAPI;
import org.alfresco.test.FailedTestListener;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners(FailedTestListener.class)
public class DocumentDetailsPropertiesTest extends AbstractUtils
{
    private static Log logger = LogFactory.getLog(DocumentDetailsPropertiesTest.class);
    protected String testUser;
    protected String siteName = "";

    @Override
    @BeforeClass(alwaysRun = true)
    public void setup() throws Exception
    {
        super.setup();
        testName = this.getClass().getSimpleName();
        logger.info("Start Tests in: " + testName);

    }

    @Test(groups = { "DataPrepDocumentLibrary" })
    public void dataPrep_AONE_2113() throws Exception
    {
        String testName = getTestName();
        String testUser = getUserNameFreeDomain(testName);
        String siteName = getSiteName(testName);

        // User
        String[] testUserInfo = new String[] { testUser };
        CreateUserAPI.CreateActivateUser(drone, ADMIN_USERNAME, testUserInfo);

        // Login
        ShareUser.login(drone, testUser, DEFAULT_PASSWORD);

        // Create Site
        ShareUser.createSite(drone, siteName, AbstractUtils.SITE_VISIBILITY_PUBLIC);
        ShareUser.openSiteDashboard(drone, siteName);

        // Upload File
        String fileName = getFileName(testName) + ".txt";
        String[] fileInfo = { fileName, DOCLIB };
        ShareUser.uploadFileInFolder(drone, fileInfo);
        ShareUser.logout(drone);

    }

    @Test(groups = "EnterpriseOnly")
    public void AONE_2113()
    {
        /** Start Test */
        testName = getTestName();

        /** Test Data Setup */
        String siteName = getSiteName(testName);
        String testUser = getUserNameFreeDomain(testName);
        String fileName = getFileName(testName) + ".txt";

        DocumentLibraryPage documentLibraryPage;

        // Login
        ShareUser.login(drone, testUser, DEFAULT_PASSWORD);

        documentLibraryPage = ShareUser.openSitesDocumentLibrary(drone, siteName);

        DetailsPage detailsPage = documentLibraryPage.selectFile(fileName);

        // Click on the Edit Properties icon in the Properties section
        EditDocumentPropertiesPage editDocumentPropertiesPage = detailsPage.selectEditProperties();

        // Try to rename file and click Save
        editDocumentPropertiesPage.setName(fileName + "edited");
        editDocumentPropertiesPage.selectSaveWithValidation().render();
        Map<String, Object> properties = detailsPage.getProperties();
        Assert.assertEquals(properties.get("Name"), fileName + "edited");
        ShareUser.logout(drone);

    }

    @Test(groups = { "DataPrepDocumentLibrary" })
    public void dataPrep_AONE_2114() throws Exception
    {
        String testName = getTestName();
        String testUser = getUserNameFreeDomain(testName);
        String siteName = getSiteName(testName);

        // User
        String[] testUserInfo = new String[] { testUser };
        CreateUserAPI.CreateActivateUser(drone, ADMIN_USERNAME, testUserInfo);

        // Login
        ShareUser.login(drone, testUser, DEFAULT_PASSWORD);

        // Create Site
        ShareUser.createSite(drone, siteName, AbstractUtils.SITE_VISIBILITY_PUBLIC);
        ShareUser.openSiteDashboard(drone, siteName);

        // Upload File
        String fileName = getFileName(testName) + ".txt";
        String[] fileInfo = { fileName, DOCLIB };
        ShareUser.uploadFileInFolder(drone, fileInfo);
        ShareUser.logout(drone);

    }

    @Test(groups = "EnterpriseOnly")
    public void AONE_2114()
    {
        /** Start Test */
        testName = getTestName();

        /** Test Data Setup */
        String siteName = getSiteName(testName);
        String testUser = getUserNameFreeDomain(testName);
        String fileName = getFileName(testName) + ".txt";

        DocumentLibraryPage documentLibraryPage;

        // Login
        ShareUser.login(drone, testUser, DEFAULT_PASSWORD);

        documentLibraryPage = ShareUser.openSitesDocumentLibrary(drone, siteName);

        DetailsPage detailsPage = documentLibraryPage.selectFile(fileName);

        // Click on the Edit Properties icon in the Properties section
        EditDocumentPropertiesPage editDocumentPropertiesPage = detailsPage.selectEditProperties();

        // Try to rename file and click Cancel
        editDocumentPropertiesPage.setName(fileName + "edited");
        editDocumentPropertiesPage.selectCancel().render();
        Map<String, Object> properties = detailsPage.getProperties();
        Assert.assertEquals(properties.get("Name"), fileName);
        ShareUser.logout(drone);

    }

}