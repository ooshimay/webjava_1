package jp.co.systena.tigerscave.webcart.controller;

import java.util.Map;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import jp.co.systena.tigerscave.webcart.model.Cart;
import jp.co.systena.tigerscave.webcart.model.DeleteForm;
import jp.co.systena.tigerscave.webcart.model.Item;
import jp.co.systena.tigerscave.webcart.model.ListForm;
import jp.co.systena.tigerscave.webcart.model.Order;
import jp.co.systena.tigerscave.webcart.service.ListService;

@Controller
public class ShoppingController {

  @Autowired
  HttpSession session;

  @Autowired
  ListService listService;

  @RequestMapping(value = "/itemlist", method = RequestMethod.GET) // URLとのマッピング
  public ModelAndView list(ModelAndView mav) {

    Map<Integer, Item> itemList = listService.getItemList();
    mav.addObject("itemList", itemList);

    mav.setViewName("ListView");
    return mav;
  }

  @RequestMapping(value="/itemlist", method = RequestMethod.POST)
  public ModelAndView order(ModelAndView mav, ListForm listForm) {


    // 注文内容をカートに追加
    Cart cart = getCart();
    cart.addOrder(listForm.getItemId(), listForm.getNum());

    // データをセッションへ保存
    session.setAttribute("cart", cart);

    return new ModelAndView("redirect:/cart");
  }

  /**
   * カートの内容を表示する
   *
   * @param mav the mav
   * @return the model and view
   */
  @RequestMapping(value = "/cart", method = RequestMethod.GET) // URLとのマッピング
  public ModelAndView cart(ModelAndView mav) {

    // セッションからカートを取得し、テンプレートに渡す
    Cart cart = getCart();
    mav.addObject("orderList", cart.getOrderList());

    // 商品一覧をテンプレートに渡す。※商品名、価格を表示するため
    Map<Integer, Item> itemListMap = listService.getItemList();
    mav.addObject("itemList", itemListMap);

    // 合計金額計算
    int totalPrice = 0;
    for (Order order : cart.getOrderList()) {
      if (itemListMap.containsKey(order.getItemId())) {
        totalPrice += order.getNum() * itemListMap.get(order.getItemId()).getPrice();
      }
    }
    mav.addObject("totalPrice", totalPrice);

    // Viewのテンプレート名設定
    mav.setViewName("CartView");

    return mav;
  }

  /**
   * 注文内容削除する
   *
   * @param mav the mav
   * @param deleteForm the delete form
   * @param bindingResult the binding result
   * @return the model and view
   */
  @RequestMapping(value = "/cart", method = RequestMethod.POST) // URLとのマッピング
  public ModelAndView deleteOrder(ModelAndView mav, @Validated DeleteForm deleteForm,
      BindingResult bindingResult) {

    if (bindingResult.getAllErrors().size() == 0) {
      //リクエストパラメータにエラーがなければ、削除処理行う

      // カートから商品を削除
      Cart cart = getCart();
      cart.deleteOrder(deleteForm.getItemId());

      // データをセッションへ保存
      session.setAttribute("cart", cart);
    }

    return new ModelAndView("redirect:/cart"); // リダイレクト
  }

  /**
   * セッションからカートを取得する
   *
   * @return the cart
   */
  private Cart getCart() {
    Cart cart = (Cart) session.getAttribute("cart");
    if (cart == null) {
      cart = new Cart();
      session.setAttribute("cart", cart);
    }

    return cart;
  }

}
