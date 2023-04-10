package web.crud.basic.basicweb.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import web.crud.basic.basicweb.Service.BoardService;
import web.crud.basic.basicweb.domain.Article;
import web.crud.basic.basicweb.domain.User;
import web.crud.basic.basicweb.form.NewArticleForm;
import web.crud.basic.basicweb.form.FullArticleForm;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/{pageNum}")
    public String showBoard(@PathVariable int pageNum,
                            @SessionAttribute(name = "loginUser", required = false) User loginUser,
                            HttpServletRequest request,
                            Model model) {

        String currentPageNum = request.getRequestURI().split("/")[2];

        List<Article> articleList = boardService.getAllArticles();
        int[] totalPageNumArr = getTotalPageNumArr(articleList);


        Collections.reverse(articleList);
        articleList = getArticlesOfPage(pageNum, articleList);

        model.addAttribute("articleList", articleList);
        model.addAttribute("totalPageNumArr", totalPageNumArr);
        model.addAttribute("currentPageNum", currentPageNum);
        model.addAttribute("totalPageNum", totalPageNumArr.length);

        return "board";
    }

    @GetMapping("/new-article")
    public String showArticleAddForm(@ModelAttribute("article") NewArticleForm form) {
        return "add-article";
    }

    @PostMapping("/new-article")
    public String addArticle(@SessionAttribute("loginUser") User user,
                            @Validated @ModelAttribute("article") NewArticleForm form,
                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "add-article";
        }

        Article newArticle = new Article();
        newArticle.setTitle(form.getTitle());
        newArticle.setContent(form.getContent());
        newArticle.setWriter(user.getId());
        newArticle.setDateTime(LocalDateTime.now());

        boardService.addNewArticle(newArticle);

        return "redirect:/board/1";
    }

    @GetMapping("/article/{articleNum}")
    public String showArticle(@PathVariable Long articleNum,
                              Model model) {
        Article article = boardService.getArticleById(articleNum);
        String writerName = boardService.getWriterName(article);
        FullArticleForm form = new FullArticleForm();
        form.setTitle(article.getTitle());
        form.setId(article.getId());
        form.setContent(article.getContent());
        form.setDateTime(article.getDateTime());
        form.setWriterId(article.getWriter());
        form.setWriterName(writerName);

        model.addAttribute("article", form);

        return "article";
    }

    @GetMapping("/edit/{articleNum}")
    public String showEditArticle(@PathVariable Long articleNum,
                              @ModelAttribute("article") FullArticleForm form) {
        return "edit-article";
    }

    @PostMapping("/edit/{articleNum}")
    public String editArticle(@PathVariable Long articleNum,
                              @ModelAttribute("article") FullArticleForm form) {
        Article article = new Article();
        article.setId(form.getId());
        article.setTitle(form.getTitle());
        article.setContent(form.getContent());
        article.setDateTime(LocalDateTime.now());
        article.setWriter(form.getWriterId());
        article = boardService.updateArticle(article);

        return "redirect:/board/article/" + articleNum;

    }

    @GetMapping("/edit/{articleNum}/delete")
    public String deleteArticle(@PathVariable Long articleNum) {
        boardService.deleteArticle(articleNum);
        return "redirect:/board/1";
    }

    private List<Article> getArticlesOfPage(int pageNum, List<Article> articleList) {
        int lastArticleIndexOfPage = pageNum * 3 - 1;
        int firstArticleIndexOfPage = lastArticleIndexOfPage - 2;
        List<Article> articlesOfPage = new ArrayList<>();
        int currentArticleIndex = firstArticleIndexOfPage;

        for (Article article : articleList) {
            if (articleList.indexOf(article) == currentArticleIndex && currentArticleIndex <= lastArticleIndexOfPage) {
                articlesOfPage.add(article);
                currentArticleIndex++;
            }
        }

        return articlesOfPage;
    }

    private int[] getTotalPageNumArr(List<Article> TotalArticles) {
        double totalArticleSize = TotalArticles.size();
        int pageNum = (int) Math.ceil(totalArticleSize/3);
        int[] pageNumArr = new int[pageNum];
        for (int i=0; i<pageNumArr.length; i++) {
            pageNumArr[i] = i+1;
        }
        return pageNumArr;
    }

}
