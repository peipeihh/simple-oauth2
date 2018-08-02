package com.pphh.oauth.sample;

import com.pphh.oauth.core.response.MessageType;
import com.pphh.oauth.core.response.Response;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Please add description here.
 *
 * @author huangyinhuang
 * @date 8/2/2018
 */
@RestController
@RequestMapping("/api/test")
public class TestController {

    @RequestMapping(value = "/fetch", method = RequestMethod.GET)
    public Response getTest() {
        return Response.mark(MessageType.SUCCESS, "test");
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public Response updateTest(@RequestParam(value = "newData") String newData) {
        return Response.mark(MessageType.SUCCESS, newData);
    }

}
