package com.majestic.pkg.api.openai.resp;

import lombok.Data;

import java.util.List;

/**
 * @author zxw
 * @Desriiption: 文本编码结果
 */
@Data
public class EmbeddingResp {

    String model;

    String object;

    List<Embedding> data;

    Usage usage;
}
