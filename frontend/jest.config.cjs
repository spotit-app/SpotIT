/** @type {import('ts-jest').JestConfigWithTsJest} */
// eslint-disable-next-line no-undef
module.exports = {
  preset: 'ts-jest',
  testEnvironment: 'jsdom',
  moduleNameMapper: {
    '^.+\\.svg$': 'jest-svg-transformer',
    '^.+\\.(css|less|scss)$': 'identity-obj-proxy'
  },
  setupFilesAfterEnv: ['<rootDir>/setupTests.ts'],
  collectCoverage: true,
  collectCoverageFrom: [
    'src/**/*.{tsx,ts}',
    '!src/{App,main}.tsx',
    '!src/**/{index,vite-env.d}.ts',
    '!src/**/types.ts',
    '!src/providers/**/*.{tsx,ts}'
  ]
};
