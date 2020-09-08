package com.cube.kiosk.modules.common.model;

/**
 * @author LMZ
 */
public interface ResultLinstener {

    void success(Object object);

    void error(Object object);

    void exception(Object object);
}
