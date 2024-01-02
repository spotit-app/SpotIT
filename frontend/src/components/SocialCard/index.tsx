import PropTypes from 'prop-types';
import { ReactNode } from 'react';

interface SocialCardProps {
  children: ReactNode;
  name: string;
  socialUrl: string;
}

function SocialCard({ children, name, socialUrl }: SocialCardProps) {
  return (
    <div className="block md:flex justify-between border-b-2 border-l-2 border-primary my-4 p-3">
      <div className="block md:flex items-center w-full">
        <div className="text-xl w-1/5 md:mx-3 font-bold">{name}</div>
        <a
          href={socialUrl}
          target="_blank"
          rel="noreferrer"
          className="text-primary flex font-bold break-all"
        >
          {socialUrl}
        </a>
      </div>
      <div className="flex justify-end">{children}</div>
    </div>
  );
}

export { SocialCard };

SocialCard.propTypes = {
  children: PropTypes.node.isRequired,
  name: PropTypes.string.isRequired,
  socialUrl: PropTypes.string.isRequired
};
