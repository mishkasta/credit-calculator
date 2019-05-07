package by.bsuir.CreditCalculator.Web.Controllers;

import by.bsuir.CreditCalculator.Web.Constants.Routes;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

// I know, this is very bad, but I hope you are able to configure servlet to redirect to index on 404

@Controller
@RequestMapping(Routes.DEFAULT)
public class IndexController {
    @RequestMapping(method = RequestMethod.GET)
    public String index() {
        return "index";
    }

    @RequestMapping(value = "/calculatecredit", method = RequestMethod.GET)
    public String calculateCredit() {
        return "index";
    }

    @RequestMapping(value = "/signin", method = RequestMethod.GET)
    public String signIn() {
        return "index";
    }

    @RequestMapping(value = "/signup", method = RequestMethod.GET)
    public String signUp() {
        return "index";
    }

    @RequestMapping(value = "/mycredits", method = RequestMethod.GET)
    public String credits() {
        return "index";
    }
}
