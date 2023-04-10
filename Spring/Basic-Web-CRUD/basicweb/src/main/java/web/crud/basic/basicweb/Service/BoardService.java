package web.crud.basic.basicweb.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.crud.basic.basicweb.domain.Article;
import web.crud.basic.basicweb.domain.ArticleMapper;
import web.crud.basic.basicweb.domain.User;
import web.crud.basic.basicweb.domain.UserMapper;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class BoardService {

    private final ArticleMapper articleMapper;
    private final UserMapper userMapper;

    @Transactional
    public List<Article> getAllArticles() {
        List<Article> articles = articleMapper.getAll();
        return articles;
    }

    @Transactional
    public boolean addNewArticle(Article article) {
        int success = articleMapper.insertArticle(article);
        log.info("article ID: {}", article.getId());
        if (success == 0) {
            return false;
        }
        return true;
    }

    @Transactional
    public Article getArticleById(Long articleId) {
        return articleMapper.getById(articleId);
    }

    @Transactional
    public String getWriterName(Article article) {
        String writerName = userMapper.getUserById(article.getWriter()).getName();
        return writerName;
    }

    @Transactional
    public Article updateArticle(Article article) {
        int zeroIsFail = articleMapper.update(article);
        Article updatedArticle = null;
        if (zeroIsFail != 0) {
            updatedArticle = articleMapper.getById(article.getId());
        }
        return updatedArticle;
    }

    @Transactional
    public void deleteArticle(Long articleId) {
        log.info("Delete Article ID: {}", articleId);
        log.info("articleMapper.getById(articleId): {}", articleMapper.getById(articleId));
        if (articleMapper.getById(articleId) == null) {
            return;
        }
        articleMapper.delete(articleId);
    }

}
