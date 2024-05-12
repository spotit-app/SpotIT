import PropTypes from 'prop-types';
import { ReactNode } from 'react';
import icons from 'assets/icons';

interface SkillCardProps {
  children?: ReactNode;
  name: string;
  level: number;
  logo?: string;
}

function SkillCard({ children, name, level, logo }: SkillCardProps) {
  const stars = [1, 2, 3, 4, 5].map((value) =>
    value <= level ? (
      <icons.IoMdStar key={value} size="35" />
    ) : (
      <icons.IoMdStarOutline key={value} size="35" />
    )
  );

  return (
    <div className="flex justify-between border-b-2 border-l-2 border-primary my-4 p-3">
      <div className="block md:flex items-center w-full">
        <div className="text-xl w-full md:w-1/4 font-bold flex items-center">
          {logo && <img src={logo} alt="Skill Logo" className="w-12 h-12 mr-3" />}
          <div>{name}</div>
        </div>

        <div className="text-primary flex">{stars}</div>
      </div>
      <div className="flex items-center">{children}</div>
    </div>
  );
}

export { SkillCard };

SkillCard.propTypes = {
  children: PropTypes.node,
  name: PropTypes.string.isRequired,
  level: PropTypes.number.isRequired,
  logo: PropTypes.string
};
