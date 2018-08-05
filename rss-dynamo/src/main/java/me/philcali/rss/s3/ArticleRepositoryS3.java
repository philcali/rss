package me.philcali.rss.s3;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkBaseException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.fasterxml.jackson.databind.ObjectMapper;

import me.philcali.db.api.QueryParams;
import me.philcali.db.api.QueryResult;
import me.philcali.db.s3.QueryRetrievalStrategy;
import me.philcali.rss.api.IArticle;
import me.philcali.rss.api.IArticleRepository;
import me.philcali.rss.api.exception.ArticleInstanceStorageException;
import me.philcali.rss.api.exception.ArticleStorageException;
import me.philcali.rss.api.model.Article;

public class ArticleRepositoryS3 implements IArticleRepository {
    private static final String S3_KEY_FORMAT = "%s/%s.json";
    private final QueryRetrievalStrategy retrieval;
    private final AmazonS3 s3;
    private final String bucketName;
    private final ObjectMapper mapper;

    public ArticleRepositoryS3(
            final String bucketName,
            final AmazonS3 s3,
            final ObjectMapper mapper) {
        this.bucketName = bucketName;
        this.s3 = s3;
        this.mapper = mapper;
        this.retrieval = QueryRetrievalStrategy.builder()
                .withPrefixField("feedId")
                .withBucketName(bucketName)
                .build();
    }

    private String toJson(final Object article) {
        try {
            return mapper.writeValueAsString(article);
        } catch (IOException ie) {
            throw new ArticleStorageException(ie);
        }
    }

    private <T> T fromJson(final InputStream input, final Class<T> inputClass) {
        try {
            return mapper.readValue(input, inputClass);
        } catch (IOException ie) {
            throw new ArticleStorageException(ie);
        }
    }

    @Override
    public void put(final String feedId, final IArticle article) {
        final String s3Key = String.format(S3_KEY_FORMAT, feedId, article.getId());
        try {

            final byte[] jsonBytes = toJson(article).getBytes(StandardCharsets.UTF_8);
            final InputStream inputStream = new ByteArrayInputStream(jsonBytes);
            final ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(jsonBytes.length);
            metadata.setContentType("application/json");
            s3.putObject(new PutObjectRequest(bucketName, s3Key, inputStream, metadata));
        } catch (SdkBaseException e) {
            throw new ArticleStorageException(e);
        }
    }

    @Override
    public Optional<IArticle> get(final String feedId, final String articleId) {
        final String s3Key = String.format(S3_KEY_FORMAT, feedId, articleId);
        try {
            final S3Object object = s3.getObject(bucketName, s3Key);
            return Optional.of(fromJson(object.getObjectContent(), Article.class));
        } catch (AmazonServiceException e) {
            if (e.getStatusCode() == 404) {
                return Optional.empty();
            }
            throw new ArticleInstanceStorageException(e);
        } catch (SdkBaseException e) {
            throw new ArticleInstanceStorageException(e);
        }
    }

    @Override
    public QueryResult<IArticle> list(final QueryParams params) {
        return retrieval.apply(params, s3).map(summary -> {
            final S3Object object = s3.getObject(new GetObjectRequest(bucketName, summary.getKey()));
            return fromJson(object.getObjectContent(), Article.class);
        });
    }

}
