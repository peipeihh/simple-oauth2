/*
 * Copyright 2018 peipeihh
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * limitations under the License.
 */
package com.pphh.oauth.utils;

import com.pphh.oauth.core.response.MessageType;
import com.pphh.oauth.exception.BaseException;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * The utils to convert object between class/types
 *
 * @author huangyinhuang
 * @date 7/2/2018
 */
public class ConvertUtil {

    public static <S, T> T convert(S s, Class<T> tClass) {
        try {
            T t = tClass.newInstance();
            BeanUtils.copyProperties(s, t);
            return t;
        } catch (Exception e) {
            throw BaseException.newException(MessageType.ERROR, "convert error");
        }
    }

    public static <S, T> T convert(S s, T t) {
        try {
            BeanUtils.copyProperties(s, t);
            return t;
        } catch (Exception e) {
            throw BaseException.newException(MessageType.ERROR, "convert error");
        }
    }

    public static <S, T> List<T> convert(Iterable<S> iterable, Class<T> tClass) {
        return StreamSupport.stream(iterable.spliterator(), false)
                .map(s -> ConvertUtil.convert(s, tClass)).collect(Collectors.toList());
    }

    public static <S, T> List<T> convert(Iterable<S> iterable, Function<? super S, ? extends T> mapper) {
        return StreamSupport.stream(iterable.spliterator(), false).map(mapper).collect(Collectors.toList());
    }

}
