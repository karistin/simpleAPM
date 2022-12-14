package com.lucas.app.handler.action.main;

import com.lucas.app.common.controller.Action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * packageName    : com.lucas.app.handler.action.main
 * fileName       : CurdAction
 * author         : lucas
 * date           : 2022-10-26
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022-10-26        lucas       최초 생성
 */
public class CurdAction implements Action {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return "RequestDispatcher:jsp/form.jsp";
    }
}
