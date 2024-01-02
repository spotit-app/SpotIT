import { PropsWithChildren, useEffect } from 'react';
import PropTypes from 'prop-types';
import { CreateUser } from 'types/profile';
import { useUser } from 'hooks/useUser';

function UserProvider({ children }: PropsWithChildren) {
  const { user, userDataError, createUser } = useUser();

  useEffect(() => {
    if (user && userDataError) {
      const newUser: CreateUser = {
        auth0Id: user!.sub!,
        email: user!.email!,
        profilePicture: user!.picture!
      };

      try {
        createUser.mutate(newUser);
      } catch (error) {
        /* do nothing */
      }
    }
  }, [user, userDataError]);

  return children;
}

export { UserProvider };

UserProvider.propTypes = {
  children: PropTypes.node.isRequired
};
