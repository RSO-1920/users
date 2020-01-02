package si.fri.rso.api.v1;

import com.kumuluz.ee.discovery.annotations.RegisterService;

import javax.annotation.security.DeclareRoles;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@RegisterService(value = "rso1920-users")
@ApplicationPath("/v1")
public class UsersApi extends Application {
}
