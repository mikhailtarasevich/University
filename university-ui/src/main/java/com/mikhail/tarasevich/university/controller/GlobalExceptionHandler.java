package com.mikhail.tarasevich.university.controller;

import com.mikhail.tarasevich.university.service.exception.EmailAlreadyExistsException;
import com.mikhail.tarasevich.university.service.exception.IncorrectRequestDataException;
import com.mikhail.tarasevich.university.service.exception.ObjectWithSpecifiedIdNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final String REFERER = "Referer";

    @ExceptionHandler({ObjectWithSpecifiedIdNotFoundException.class, IncorrectRequestDataException.class,
            EmailAlreadyExistsException.class})
    public ModelAndView handleException(Exception ex, HttpServletRequest req) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("exception", ex.getMessage());
        mav.addObject("url", req.getRequestURL());
        mav.addObject("referer", req.getHeader(REFERER));
        mav.setViewName("globalExceptionHandler/error");

        String referer = req.getHeader(REFERER);
        if (referer != null) {
            mav.addObject("previousUrl", referer);
        }

        return mav;
    }

    @ExceptionHandler({Exception.class, RuntimeException.class})
    public ModelAndView commonException(Exception ex, HttpServletRequest req) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("exception", ex);
        mav.addObject("url", req.getRequestURL());
        mav.setViewName("globalExceptionHandler/common-error");

        String referer = req.getHeader(REFERER);
        if (referer != null) {
            mav.addObject("previousUrl", referer);
        }

        return mav;
    }

}
