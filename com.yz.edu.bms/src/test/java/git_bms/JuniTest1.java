package git_bms;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.yz.app.BmsApplication;
import com.yz.util.TokenUtil;
import com.yz.util.TokenUtil.Secure;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BmsApplication.class)
@WebAppConfiguration
public class JuniTest1 {
//	@Autowired
//	private OaCommissionService service;
//	@Autowired
//	private OaExpenseService service2;
//	@Test
//	public void test111(){
////		service.myCommission("1202");
//		OaMonthExpense ex=new OaMonthExpense();
//		ex.setEmpId("1202");
//		ex.setMonth("2018-07");
//		service2.getBalance(ex);
//	}
	
	@Test
	public void test(){
		String string="XWMEncl2FFrXqxFvXqvj34VqaH5g6oPzorBTjP6vpu2FlZptBMLjRN6YLD3D1EeVtPPBTeCXuzs=";
		Secure s = TokenUtil.convert(string);
		if(s.getUserId().equals("")){
			
		}
	}
}
