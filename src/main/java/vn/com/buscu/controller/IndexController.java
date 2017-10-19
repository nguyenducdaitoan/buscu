package vn.com.buscu.controller;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.context.MessageSource;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.com.buscu.bean.FileForm;
import vn.com.buscu.bean.Pager;
import vn.com.buscu.bean.Product;
import vn.com.buscu.common.BuscuConst;
import vn.com.buscu.bean.ErrorException;
import vn.com.buscu.service.ProductService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import vn.com.buscu.validator.FileValidator;
import vn.com.buscu.validator.ReadFile;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * IndexController Class
 *
 * @version 1.0
 * @author ToanNDD
 */
@Controller
public class IndexController {

    @Autowired
    private ProductService productService;

    @Autowired
    FileValidator validator;

    @Autowired
    ReadFile readFile;

    private final MessageSource messageSource;

    protected  List<ErrorException> errorExceptionList;

    List<Product> productErrorList = new ArrayList<Product>();

    List<Product> productList = new ArrayList<Product>();

    private final Logger logger = Logger.getLogger(this.getClass());

    /**
     * Constructor
     *
     * @param messageSource
     */
    @Autowired
    public IndexController(MessageSource messageSource) {
        this.messageSource = messageSource;
        this.errorExceptionList = new ArrayList<>();
    }

    /**
     * index method
     *
     * @param pager
     * @param model
     * @param request
     * @param response
     * @return String
     */
    @RequestMapping(value = BuscuConst.REQ_INDEX, method = RequestMethod.GET)
    public String index(Pager pager, Model model, HttpServletRequest request, HttpServletResponse response){
        return BuscuConst.LIST_PRODUCT;
    }

    /**
     * listProduct method
     *
     * @param pager
     * @param model
     * @param request
     * @param response
     * @return String
     */
    @RequestMapping(value = BuscuConst.REQ_LIST_PRODUCT, method = RequestMethod.GET)
    public String listProduct(Pager pager, Model model, HttpServletRequest request, HttpServletResponse response){
        List<Product> pagingProductList = new ArrayList<Product>();

        int pageNumber = BuscuConst.VAL_1;
        int recordsPerPage = BuscuConst.VAL_10;
        int total = BuscuConst.VAL_0;
        int noOfPages;

        if(pager.getPage() == BuscuConst.VAL_0) {
            pageNumber = BuscuConst.VAL_1;
        } else {
            pageNumber = pager.getPage();
        }
        try {
            total = productService.countTotalProduct();

            pagingProductList = productService.selectProductByPage(pageNumber);

        } catch (Exception e) {
            logger.error(e);
        }

        noOfPages = (int) Math.ceil(total * BuscuConst.VAL_1_0 / recordsPerPage);

        model.addAttribute(BuscuConst.PAGING_PRODUCT_LIST, pagingProductList);
        model.addAttribute(BuscuConst.ERROR_PRODUCT_LIST, productErrorList);
        model.addAttribute(BuscuConst.NO_OF_PAGE, noOfPages);
        model.addAttribute(BuscuConst.CURRENT_PAGE, pageNumber);

        model.addAttribute(BuscuConst.ERROR_EXCEPTION_LIST, errorExceptionList);

        return BuscuConst.LIST_PRODUCT;
    }

    /**
     * getForm Method
     *
     * @param model
     * @return String
     */
    @RequestMapping(method = RequestMethod.GET)
    public String getForm(Model model) {
        FileForm fileModel = new FileForm();
        model.addAttribute(BuscuConst.FILE, fileModel);
        return BuscuConst.FILE;
    }

    /**
     * fileUploaded Method
     *
     * @param csvFile
     * @param model
     * @param file
     * @param result
     * @param request
     * @param response
     * @return String
     */
    @RequestMapping(value = BuscuConst.REQ_UPLOAD, method = RequestMethod.POST)
    public String fileUploaded(@ModelAttribute("csvFile") FileForm csvFile, Model model, @Validated FileForm file,
                               BindingResult result, HttpServletRequest request, HttpServletResponse response) {
        errorExceptionList.clear();

        if (result.hasErrors()) {
            return BuscuConst.FILE;
        } else {

            MultipartFile multipartFile = file.getFile();
            String rootPath = request.getSession().getServletContext().getRealPath(BuscuConst.SLASH);
            File dir = new File(rootPath + File.separator + BuscuConst.UPLOADED_FILE);

            System.out.println(rootPath + File.separator + BuscuConst.UPLOADED_FILE);

            if (!dir.exists()) {
                dir.mkdirs();
            }
            File serverFile = new File(dir.getAbsolutePath() + File.separator + multipartFile.getOriginalFilename());

            try (InputStream is = multipartFile.getInputStream();
                 BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile))) {

                int i;
                while ((i = is.read()) != BuscuConst.VAL_MINUS_1) {
                    stream.write(i);
                }
                stream.flush();
            } catch (IOException e) {
                errorExceptionList.add(new ErrorException(
                        BuscuConst.ERROR_CODE_ERR0001, messageSource.getMessage(BuscuConst.ERROR_CODE_ERR0001, null, null)));
                return BuscuConst.FILE_EXEPTION;
            }
            ReadFile.readFile(serverFile, productService, productErrorList);
        }

        try {
            response.sendRedirect(BuscuConst.LIST_PRODUCT);
        } catch (IOException e) {
            e.printStackTrace();
            errorExceptionList.add(new ErrorException(
                    BuscuConst.ERROR_CODE_WRN0002,messageSource.getMessage(BuscuConst.ERROR_CODE_WRN0002, null, null)));
        }

        errorExceptionList.add(new ErrorException(
                BuscuConst.ERROR_CODE_INF0007, messageSource.getMessage(BuscuConst.ERROR_CODE_INF0007, null, null)));

        return BuscuConst.LIST_PRODUCT;
    }

    /**
     * process Method
     *
     * @param request
     * @param response
     * @return void
     */
    @RequestMapping(value = BuscuConst.REQ_PROCESS, method = RequestMethod.GET)
    public void process(HttpServletRequest request, HttpServletResponse response) {

        int fromPage = Integer.parseInt(request.getParameter("fromPage"));
        int toPage = Integer.parseInt(request.getParameter("toPage"));

        System.setProperty("webdriver.chrome.driver", "C:\\chromedriver.exe");

        //WebDriver driver = new ChromeDriver();

        ChromeOptions options = new ChromeOptions();
        options.addExtensions(new File("C:\\extension_2_6_1.crx"));
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(ChromeOptions.CAPABILITY, options);

        //start driver in incognito mode
        capabilities.setCapability(CapabilityType.ForSeleniumServer.ENSURING_CLEAN_SESSION, true);
        capabilities.setCapability("chrome.switches", Arrays.asList("--incognito"));
        ChromeDriver driver = new ChromeDriver(capabilities);
        driver.manage().window().maximize();

        driver.get("https://www.amazon.com");
        WebElement element1 = driver.findElement(By.xpath("//*[@id=\"nav-link-shopall\"]"));
        //WebElement element2 = driver.findElement(By.xpath("//*[@id=\"nav-signin-tooltip\"]/a/span"));
        Actions actions1 = new Actions(driver);
        actions1.moveToElement(element1);
        actions1.click();
        //element2.click();
        driver.get("https://www.amazon.com/ap/signin?_encoding=UTF8&openid.assoc_handle=usflex&openid.claimed_id=http%3A%2F%2Fspecs.openid.net%2Fauth%2F2.0%2Fidentifier_select&openid.identity=http%3A%2F%2Fspecs.openid.net%2Fauth%2F2.0%2Fidentifier_select&openid.mode=checkid_setup&openid.ns=http%3A%2F%2Fspecs.openid.net%2Fauth%2F2.0&openid.ns.pape=http%3A%2F%2Fspecs.openid.net%2Fextensions%2Fpape%2F1.0&openid.pape.max_auth_age=0&openid.return_to=https%3A%2F%2Fwww.amazon.com%2F%3Fref_%3Dnav_custrec_signin");
        WebElement emailField = driver.findElement(By.id("ap_email")); emailField.sendKeys("nguyenducdaitoan88@gmail.com");
        WebElement passwordField = driver.findElement(By.id("ap_password")); passwordField.sendKeys("pass@word");
        WebElement signinButton=driver.findElement(By.id("signInSubmit"));
        signinButton.click();

        String branch = "";
        String title = "";
        String priceTmp = "";
        Double price = 0d;
        String rankTmp = "";
        Double rank = 0d;
        String asin = "";
        String image = "";

        int page = 0;
        for ( int i = fromPage; i <= toPage; i++ ) {
            page = i;
            int last = (47*i) + i;
            int first = last - 48 ;
            Product product = null;
            try {
                Thread.sleep(5000);//1000 milliseconds is one second.
                driver.get("https://www.amazon.com/s/ref=sr_pg_2?rh=n%3A7141123011%2Cn%3A7147445011%2Cn%3A12035955011%2Cp_6%3AATVPDKIKX0DER&page="+ page +"&bbn=12035955011&hidden-keywords=ORCA&ie=UTF8&qid=1482475994");
                System.out.println( "current page : " + page ) ;
                System.out.println( "current first : " + first ) ;
                System.out.println( "current last : " + (last - 1) ) ;
                for (int j = 1; j < 5; j++ ) {
                    try {
                        Thread.sleep(5000);//1000 milliseconds is one second.
                        //jse.executeScript("window.scrollBy(0,1200)", "");
                        //move to the target item on the screen
                        int current = first;
                        int tmp = (j * 11) + 1;
                        current += tmp;
                        System.out.println( current ) ;
                        if (driver.findElements(By.xpath("//*[@id=\"result_" +current+ "\"]/div/div[4]/div[2]/span")).size() < 0) {
                            Thread.sleep(10000);//1000 milliseconds is one second.
                        }
                        //if (driver.findElements(By.xpath("//*[@id=\"result_" + first + "\"]/div/div[4]/div[2]/span")).size() > 0) {
						if (!driver.findElements(By.xpath("//*[@id=\"result_" + first + "\"]/div/div[4]/div[2]/span")).isEmpty()) {
                            WebElement element = driver.findElement(By.xpath("//*[@id=\"result_" + current + "\"]/div/div[4]/div[2]/span"));
                            Actions actions = new Actions(driver);
                            actions.moveToElement(element);
                            // actions.click();
                            actions.perform();
                        }
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                }

            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }

            while (first < last) {
                if (driver.findElements(By.xpath("//*[@id=\"result_" + first + "\"]/div/div[4]/div[2]/span")).size() > 0) {
                    //old code to get asin
                    asin = driver.findElement(By.xpath("//*[@id=\"result_" + first + "\"]/div/div[4]/div[2]/span")).getText();
                }

                if (driver.findElements(By.xpath("//*[@id=\"result_" + first + "\"]/div/div[2]/div/a/span")).size() > 0) {
                    branch = driver.findElement(By.xpath("//*[@id=\"result_" + first + "\"]/div/div[2]/div/a/span")).getText();
                }
                if (driver.findElements(By.xpath("//*[@id=\"result_" + first + "\"]/div/div[3]/div[1]/a/h2")).size() > 0) {
                    title = driver.findElement(By.xpath("//*[@id=\"result_" + first + "\"]/div/div[3]/div[1]/a/h2")).getText();
                }

                if (driver.findElements(By.xpath("//*[@id=\"result_" + first + "\"]/div/div[3]/div[2]/a/span/span/span/span")).size() > 0) {
                    //String priceWhole = driver.findElement(By.xpath("//*[@id=\"result_" + first + "\"]/div/div[3]/div[2]/a/span/span/span/span")).getText();
                    //String priceFractional = driver.findElement(By.xpath("//*[@id=\"result_" + first + "\"]/div/div[3]/div[2]/a/span/span/span/sup[2]")).getText();
                    //System.out.println(priceWhole);
                    //System.out.println(priceFractional);
                    //price = Double.parseDouble(priceWhole + "." + priceFractional);
                    price = Double.parseDouble(driver.findElement(By.xpath("//*[@id=\"result_" + first + "\"]/div/div[3]/div[2]/a/span/span/span/span")).getText() + "." + driver.findElement(By.xpath("//*[@id=\"result_" + first + "\"]/div/div[3]/div[2]/a/span/span/span/sup[2]")).getText());
//                    System.out.println(price);
                }

                //old code to get price
                //if (driver.findElements(By.xpath("//*[@id=\"result_" + first + "\"]/div/div[3]/div[2]/a/span")).size() > 0) {
                    //priceTmp = driver.findElement(By.xpath("//*[@id=\"result_" + first + "\"]/div/div[3]/div[2]/a/span")).getText();
                    //String priceC = priceTmp.replaceAll("from ","");
                    //System.out.println(priceC);
                    //price = Double.parseDouble(priceC.substring(1, priceC.length()));
                //}

                if (driver.findElements(By.xpath("//*[@id=\"result_"+ first +"\"]/div/div[4]/div[1]/span")).size() > 0) {
                    rankTmp = driver.findElement(By.xpath("//*[@id=\"result_" + first + "\"]/div/div[4]/div[1]/span")).getText();
                    //System.out.println(saleRank);
                    if (rankTmp != null) {
                        String saleRank = rankTmp.replaceAll(",","");
                        rank = Double.parseDouble(saleRank.substring(1, saleRank.length()));
                    }
                }
                //StringBuilder builder = new StringBuilder(asin);
                if (driver.findElements(By.xpath("//*[@id=\"rot-" + asin.substring(6, asin.length()) + "\"]/div/a/div[1]/img")).size() > 0 ) {
                    image = driver.findElement(By.xpath("//*[@id=\"rot-" + asin.substring(6, asin.length()) + "\"]/div/a/div[1]/img")).getAttribute("src").toString();
                }

//                System.out.println(branch);
//                System.out.println(title);
//                System.out.println(price);
//                System.out.println(rank);
//                System.out.println(asin);
//                System.out.println(image);

                //String saleRank = rank.replace(',','.');
                //product = new Product(asin.substring(6, asin.length()), branch, title, Double.parseDouble(price.substring(price.length() - 5, price.length())), Double.parseDouble(saleRank.substring(1, saleRank.length())), image, image);
                product = new Product(asin.substring(6, asin.length()) , branch, title, price, rank, image, image);
                productList.add(product);
                first++;
            }

            // add listProduct to Database
            try {
                productService.insert(productList);
            } catch (Exception e) {
                e.printStackTrace();
            }
            // clear listProduct
            productList.clear();
        }
    }
}
