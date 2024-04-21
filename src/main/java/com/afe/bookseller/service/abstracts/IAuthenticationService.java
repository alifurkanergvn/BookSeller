package com.afe.bookseller.service.abstracts;

import com.afe.bookseller.model.User;


public interface IAuthenticationService
{
    User signInAndReturnJWT(User signInRequest);
}
